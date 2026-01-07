package server.web.casa.app.user.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import server.web.casa.app.user.domain.model.User
import server.web.casa.app.user.infrastructure.persistence.entity.*
import server.web.casa.app.user.infrastructure.persistence.repository.*
import server.web.casa.security.*
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import org.springframework.transaction.annotation.Transactional
import server.web.casa.adaptater.provide.twilio.TwilioService
import server.web.casa.app.actor.application.service.PersonService
import server.web.casa.app.user.domain.model.AccountUser
import server.web.casa.app.user.domain.model.UserDto
import server.web.casa.app.user.domain.model.UserFullDTO
import server.web.casa.app.user.domain.model.request.AccountRequest
import server.web.casa.app.user.domain.model.request.VerifyRequest
import server.web.casa.app.user.infrastructure.persistence.mapper.*
import server.web.casa.utils.*
import java.security.MessageDigest
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.time.ExperimentalTime
//sudo docker run --name casa-db -e POSTGRES_PASSWORD=root -e POSTGRES_DB=testdb e- POSTGRES_USERNAME=postgres -p 5434:5432 -d postgres
//https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-22-04
@Service
@Profile(Mode.DEV)
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val twilio : TwilioService,
    private val serviceMultiAccount: AccountUserService,
    private val typeAccountService: TypeAccountService,
    private val accountService: AccountService,
    private val servicePerson: PersonService,
    private val repositoryUserAccount : AccountUserRepository
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )
    @OptIn(ExperimentalTime::class)
    suspend fun register(user: User, accountItems: List<AccountRequest>): Pair<UserDto?, String> {
        var phone = ""
            if (user.phone != null) {
                phone =  normalizeAndValidatePhoneNumberUniversal(user.phone) ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Ce numero n'est pas valide.")
        }

        if(userRepository.findByPhoneOrEmail(phone) != null) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Cet identifiant existe dans la plateforme.")
        }

        if (user.email != null){
            if(user.email.isNotEmpty()){
                if(userRepository.findByPhoneOrEmail(user.email) != null) {
                    throw ResponseStatusException(HttpStatus.CONFLICT, "Cette adresse existe dans la plateforme.")
                }
            }
        }

        val entity = UserEntity(
            password = hashEncoder.encode(user.password),
            email = user.email,
            username = user.username,
            phone = phone,
            city = user.city,
            country = user.country
        )
        log.info("Creating user ${user.userId}")
        val savedEntity = userRepository.save(entity)
//        throw Exception("stop ***")
        accountItems.forEach {
            serviceMultiAccount.save(
                AccountUser(
                    userId = savedEntity.userId!!,
                    accountId = it.typeAccount
                )
            )
        }

        val newAccessToken = jwtService.generateAccessToken(savedEntity.userId!!.toHexString())
//        val newRefreshToken = jwtService.generateRefreshToken(user.userId.toHexString())
//        storeRefreshToken(savedEntity.userId, newRefreshToken)
        val userData : UserDto = savedEntity.toDomain()
        val result = Pair(userData,newAccessToken)
        return result
    }
    suspend fun login(identifier: String, password: String): Pair<TokenPair, UserFullDTO>  =
        coroutineScope {
            var validIdentifier = normalizeAndValidatePhoneNumberUniversal(identifier)
            if (isEmailValid(identifier)){
                validIdentifier = identifier
            }

            val user = userRepository.findByPhoneOrEmail(validIdentifier.toString())
                ?: throw ResponseStatusException(HttpStatusCode.valueOf(403), "Invalid credentials.")

            if(!hashEncoder.matches(password, user.password.toString())) {
                throw ResponseStatusException(HttpStatusCode.valueOf(403), "Invalid credentials.")
            }

            log.info("Logging into user ${user.userId}")
            val newAccessToken = jwtService.generateAccessToken(user.userId!!.toHexString())
            val newRefreshToken = jwtService.generateRefreshToken(user.userId.toHexString())
            val accounts = serviceMultiAccount.getAll().filter { it.userId == user.userId }.toList()
            val accountMultiple: List<AccountDTO> =  accounts.map {
                val data = accountService.findByIdAccount(it.accountId)
                AccountDTO(
                    id = data.id,
                    name = data.name,
                    typeAccount = typeAccountService.findByIdTypeAccount(data.typeAccountId)
                )
            }.toList()
            val profile = servicePerson.findByIdPersonUser(user.userId)
            storeRefreshToken(user.userId, newRefreshToken)
            val result = Pair(
                TokenPair(accessToken = newAccessToken, refreshToken = newRefreshToken),
                UserFullDTO(user.toDomain(), accountMultiple,profile))
            result
     }
    suspend fun generateOTP(identifier: String): Triple<String?, String, String> {
        var validIdentifier = normalizeAndValidatePhoneNumberUniversal(identifier)
        if (isEmailValid(identifier)){
            validIdentifier = identifier
        }
        val user = userRepository.findByPhoneOrEmail(validIdentifier.toString())
            ?: throw ResponseStatusException(HttpStatusCode.valueOf(403), "Idenfiant invalide.")
       val status = twilio.generateVerifyOTP(identifier)
       return Triple(status, if (status == "pending") "Votre code de vérification a été envoyé avec suucès" else "Erreur numero non prises en charge",identifier)
    }
    suspend fun verifyOTP(user : VerifyRequest): Pair<Long, String?> {
        val userSecurity = userRepository.findByPhoneOrEmail(user.identifier)
            ?: throw ResponseStatusException(HttpStatusCode.valueOf(403), "Idenfiant invalide.")
       return Pair(userSecurity.userId!!,twilio.checkVerify(code = user.code, contact = user.identifier))
    }
    suspend fun changePassword(id : Long,new : String): UserDto {
        val data = userRepository.findById(id)
        if (data != null) {
            data.password = hashEncoder.encode(new)
            val updatedUser = userRepository.save(data)
            return updatedUser.toDomain()
        }
        throw ResponseStatusException(HttpStatusCode.valueOf(403), "ID invalide.")
    }
    @Transactional
    suspend fun refresh(refreshToken: String): TokenPair {
        if(!jwtService.validateRefreshToken(refreshToken)) {
            throw ResponseStatusException(HttpStatusCode.valueOf(403), "Invalid refresh token.")
        }
        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findById(userId.toLong()) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(403),
            "Invalid refresh token."
        )

        val hashed = hashToken(refreshToken)
            refreshTokenRepository.findByUserIdAndHashedToken(user.userId!!, hashed)
                ?: throw ResponseStatusException(
                    HttpStatusCode.valueOf(403),
                    "Refresh token not recognized (maybe used or expired?)"
                )
            refreshTokenRepository.deleteByUserIdAndHashedToken(user.userId, hashed)
            val newAccessToken = jwtService.generateAccessToken(userId)
            val newRefreshToken = jwtService.generateRefreshToken(userId)
            storeRefreshToken(user.userId, newRefreshToken)
            return TokenPair(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken
            )
    }
    @OptIn(ExperimentalTime::class)
    private suspend fun storeRefreshToken(userId: Long, rawRefreshToken: String) {
        val hashed = hashToken(rawRefreshToken)
        val expiryMs = jwtService.refreshTokenValidityMs
        val instant = Instant.now().plusMillis(expiryMs)
        val zoneId = ZoneId.systemDefault()
        val expiresAt =  LocalDateTime.ofInstant(instant, zoneId)
        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashed
            )
        )
    }
    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}