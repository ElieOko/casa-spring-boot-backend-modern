package server.web.casa.app.user.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
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
import server.web.casa.app.actor.infrastructure.persistence.repository.PersonRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.repository.PrestationRepository
import server.web.casa.app.property.infrastructure.persistence.repository.BureauRepository
import server.web.casa.app.property.infrastructure.persistence.repository.HotelRepository
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyRepository
import server.web.casa.app.property.infrastructure.persistence.repository.SalleFestiveRepository
import server.web.casa.app.property.infrastructure.persistence.repository.SalleFuneraireRepository
import server.web.casa.app.property.infrastructure.persistence.repository.TerrainRepository
import server.web.casa.app.user.domain.model.*
import server.web.casa.app.user.domain.model.request.*
import server.web.casa.app.user.infrastructure.persistence.mapper.*
import server.web.casa.utils.*
import java.security.MessageDigest
import java.time.*
import java.util.*
import kotlin.time.ExperimentalTime
//sudo docker run --name casa-db -e POSTGRES_PASSWORD=root -e POSTGRES_DB=testdb e- POSTGRES_USERNAME=postgres -p 5434:5432 -d postgres
//https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-22-04
@Service
@Profile(Mode.DEV)
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val prestation: PrestationRepository,
    private val property: PropertyRepository,
    private val hotel: HotelRepository,
    private val bureau: BureauRepository,
    private val terrain: TerrainRepository,
    private val festive: SalleFestiveRepository,
    private val funeraire: SalleFuneraireRepository,
    private val person: PersonRepository,
    private val hashEncoder: HashEncoder,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val twilio : TwilioService,
    private val serviceMultiAccount: AccountUserService,
    private val typeAccountService: TypeAccountService,
    private val accountService: AccountService,
    private val servicePerson: PersonService,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )
    @OptIn(ExperimentalTime::class)
    suspend fun register(user: User, accountItems: List<AccountRequest>): Pair<UserDto?, String> {
        var phone = normalizeAndValidatePhoneNumberUniversal(user.phone)
        var state = false
            if (user.phone != null) {
                if (user.phone.isNotEmpty()){
                    phone =  normalizeAndValidatePhoneNumberUniversal(user.phone) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce numero n'est pas valide.")
                    if(userRepository.findByPhoneOrEmail(phone) != null) throw ResponseStatusException(HttpStatus.CONFLICT, "Ce numéro de téléphone est déjà associé à un compte existant.")
                    state = true
                }
            }
        if (user.email != null){
            if(user.email.isNotEmpty()){
                if(userRepository.findByPhoneOrEmail(user.email) != null) throw ResponseStatusException(HttpStatus.CONFLICT, "Cette adresse mail est déjà associé à un compte existant.")
                state = true
            }
        }
        if (!state) throw ResponseStatusException(HttpStatus.CONFLICT, "Vous devez renseigner l'email ou le phone.")
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
            serviceMultiAccount.save(AccountUser(userId = savedEntity.userId!!, accountId = it.typeAccount))
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
            if (isEmailValid(identifier)) validIdentifier = identifier
            val user = userRepository.findByPhoneOrEmail(validIdentifier.toString()) ?: throw ResponseStatusException(HttpStatusCode.valueOf(403), "Invalid credentials.")
            if(!hashEncoder.matches(password, user.password.toString())) throw ResponseStatusException(HttpStatusCode.valueOf(403), "Invalid credentials.")
            log.info("Logging into user ${user.userId}")
            val newAccessToken = jwtService.generateAccessToken(user.userId!!.toHexString())
            val newRefreshToken = jwtService.generateRefreshToken(user.userId.toHexString())
            val accounts = serviceMultiAccount.getAll().filter { it.userId == user.userId }.toList()
            val accountMultiple: List<AccountDTO> =  accounts.map {
                val data = accountService.findByIdAccount(it.accountId)
                AccountDTO(id = data.id, name = data.name, typeAccount = typeAccountService.findByIdTypeAccount(data.typeAccountId))
            }.toList()
            val profile = servicePerson.findByIdPersonUser(user.userId)
            storeRefreshToken(user.userId, newRefreshToken)
            val result = Pair(
                TokenPair(accessToken = newAccessToken, refreshToken = newRefreshToken),
                UserFullDTO(user.toDomain(), accountMultiple,profile)
            )
            result
     }
    suspend fun generateOTP(identifier: String): Triple<String?, String, String> {
       var validIdentifier = normalizeAndValidatePhoneNumberUniversal(identifier)
       if (isEmailValid(identifier)) validIdentifier = identifier
       userRepository.findByPhoneOrEmail(validIdentifier.toString()) ?: throw ResponseStatusException(HttpStatusCode.valueOf(403), "Idenfiant invalide.")
       val status = twilio.generateVerifyOTP(identifier)
       return Triple(status, if (status == "pending") "Votre code de vérification a été envoyé avec suucès" else "Erreur numero non prises en charge",identifier)
    }
    suspend fun verifyOTP(user : VerifyRequest): Pair<Long, String?> {
       val userSecurity = userRepository.findByPhoneOrEmail(user.identifier) ?: throw ResponseStatusException(HttpStatusCode.valueOf(403), "Idenfiant invalide.")
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
    suspend fun lockedOrUnlocked(userId: Long, isLock: Boolean = true) : Boolean = coroutineScope{
        log.info("user method -> $userId")
    val state = when {
            userRepository.findById(userId) != null -> {
                log.info("in")
                if (prestation.findByUser(userId).toList().isNotEmpty())
                    prestation.setUpdateIsAvailable(userId, !isLock)
                if (bureau.findAllByUser(userId).toList().isNotEmpty())
                    bureau.setUpdateIsAvailable(userId, !isLock)
                if (property.findAllByUser(userId).toList().isNotEmpty())
                    property.setUpdateIsAvailable(userId, !isLock)
                if (funeraire.findAllByUser(userId).toList().isNotEmpty())
                    funeraire.setUpdateIsAvailable(userId, !isLock)
                if (festive.findAllByUser(userId).toList().isNotEmpty())
                    festive.setUpdateIsAvailable(userId, !isLock)
                if (terrain.findAllByUser(userId).toList().isNotEmpty())
                    terrain.setUpdateIsAvailable(userId, !isLock)
                if (hotel.getAllByUser(userId).toList().isNotEmpty())
                    hotel.setUpdateIsAvailable(userId, !isLock)
                if (person.findByUser(userId) != null)
                    person.isLock(userId, isLock)
                userRepository.isLock(userId, isLock)
                true
            }
            else-> false
        }
        state
    }
    @Transactional
    suspend fun refresh(refreshToken: String): TokenPair {
        if(!jwtService.validateRefreshToken(refreshToken)) throw ResponseStatusException(HttpStatusCode.valueOf(403), "Invalid refresh token.")
        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findById(userId.toLong()) ?: throw ResponseStatusException(HttpStatusCode.valueOf(403), "Invalid refresh token.")
        val hashed = hashToken(refreshToken)
        refreshTokenRepository.findByUserIdAndHashedToken(user.userId!!, hashed) ?: throw ResponseStatusException(HttpStatusCode.valueOf(403), "Refresh token not recognized (maybe used or expired?)")
        refreshTokenRepository.deleteByUserIdAndHashedToken(user.userId, hashed)
        val newAccessToken = jwtService.generateAccessToken(userId)
        val newRefreshToken = jwtService.generateRefreshToken(userId)
        storeRefreshToken(user.userId, newRefreshToken)
        return TokenPair(accessToken = newAccessToken, refreshToken = newRefreshToken)
    }
    @OptIn(ExperimentalTime::class)
    private suspend fun storeRefreshToken(userId: Long, rawRefreshToken: String) {
        val hashed = hashToken(rawRefreshToken)
        val expiryMs = jwtService.refreshTokenValidityMs
        val instant = Instant.now().plusMillis(expiryMs)
        val zoneId = ZoneId.systemDefault()
        val expiresAt =  LocalDateTime.ofInstant(instant, zoneId)
        refreshTokenRepository.save(RefreshToken(userId = userId, expiresAt = expiresAt, hashedToken = hashed))
    }
    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}