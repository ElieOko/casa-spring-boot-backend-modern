package server.web.casa.app.user.application.service

import org.springframework.context.annotation.Profile
import server.web.casa.app.user.domain.model.User
import server.web.casa.app.user.infrastructure.persistence.entity.*
import server.web.casa.app.user.infrastructure.persistence.repository.*
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper
import server.web.casa.security.*
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import org.springframework.transaction.annotation.Transactional
import server.web.casa.adaptater.provide.twilio.TwilioService
import server.web.casa.app.user.domain.model.UserDto
import server.web.casa.app.user.domain.model.request.VerifyRequest
import server.web.casa.app.user.infrastructure.persistence.mapper.TypeAccountMapper
import server.web.casa.utils.*
import java.security.MessageDigest
import java.time.Instant
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
    private val mapper: UserMapper,
    private val mapperAccount: TypeAccountMapper,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val twilio : TwilioService
) {
    data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )

    @OptIn(ExperimentalTime::class)
     suspend fun register(user : User): Pair<UserDto?, String> {
         val phone =  normalizeAndValidatePhoneNumberUniversal(user.phone) ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Ce numero n'est pas valide.")

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
            userId = 0,
            password = hashEncoder.encode(user.password),
            typeAccount = mapperAccount.toEntity(user.typeAccount) ,
            email = user.email,
            username = user.username,
            phone = phone,
            city = user.city,
            country = user.country
        )
        val savedEntity = userRepository.save(entity)
        val newAccessToken = jwtService.generateAccessToken(savedEntity.userId.toHexString())
        val userData : UserDto? = mapper.toDomain(savedEntity)
        val result = Pair(userData,newAccessToken)
        return result
    }

    suspend fun login(identifier: String, password: String): Pair<TokenPair, UserDto?> {
        var validIdentifier = normalizeAndValidatePhoneNumberUniversal(identifier)
        if (isEmailValid(identifier)){
            validIdentifier = identifier
        }

        val user = userRepository.findByPhoneOrEmail(validIdentifier.toString())
            ?: throw ResponseStatusException(HttpStatusCode.valueOf(403), "Invalid credentials.")

        if(!hashEncoder.matches(password, user.password.toString())) {
            throw ResponseStatusException(HttpStatusCode.valueOf(403), "Invalid credentials.")
        }

        val newAccessToken = jwtService.generateAccessToken(user.userId.toHexString())
        val newRefreshToken = jwtService.generateRefreshToken(user.userId.toHexString())

        storeRefreshToken(user.userId, newRefreshToken)
        val result = Pair(
            TokenPair(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken
            )
            ,mapper.toDomain(user))
        return result
    }

    fun generateOTP(identifier: String): Triple<String?, String, String> {
        var validIdentifier = normalizeAndValidatePhoneNumberUniversal(identifier)
        if (isEmailValid(identifier)){
            validIdentifier = identifier
        }
        val user = userRepository.findByPhoneOrEmail(validIdentifier.toString())
            ?: throw ResponseStatusException(HttpStatusCode.valueOf(403), "Idenfiant invalide.")
       val status = twilio.generateVerifyOTP()
       return Triple(status, if (status == "pending") "Votre code de vérification a été envoyé avec suucès" else "Erreur",identifier)
    }

    fun verifyOTP(user : VerifyRequest): Pair<Long, String?> {
        val userSecurity = userRepository.findByPhoneOrEmail(user.identifier)
            ?: throw ResponseStatusException(HttpStatusCode.valueOf(403), "Idenfiant invalide.")
       return Pair(userSecurity.userId,twilio.checkVerify(code = user.code, contact = user.identifier))
    }
    suspend fun changePassword(id : Long,new : String): UserDto? {
        val data = userRepository.findById(id).orElse(null)
        data.password = hashEncoder.encode(new)
        val updatedUser = userRepository.save(data)
        return mapper.toDomain(updatedUser)
    }

    @Transactional
    suspend fun refresh(refreshToken: String): TokenPair {
        if(!jwtService.validateRefreshToken(refreshToken)) {
            throw ResponseStatusException(HttpStatusCode.valueOf(403), "Invalid refresh token.")
        }
        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findById(userId.toLong()).orElseThrow{
            ResponseStatusException(
                HttpStatusCode.valueOf(403),
                "Invalid refresh token."
            )
        }
            val hashed = hashToken(refreshToken)
            refreshTokenRepository.findByUserIdAndHashedToken(user.userId, hashed)
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
        val expiresAt = Instant.now().plusMillis(expiryMs)
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