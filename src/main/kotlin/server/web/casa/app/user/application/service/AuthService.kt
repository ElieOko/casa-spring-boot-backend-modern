package server.web.casa.app.user.application.service

import org.springframework.context.annotation.Profile
import server.web.casa.app.user.domain.model.User
import server.web.casa.app.user.infrastructure.persistence.entity.*
import server.web.casa.app.user.infrastructure.persistence.repository.*
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper
import server.web.casa.security.*
import org.springframework.http.*
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import org.springframework.transaction.annotation.Transactional
import server.web.casa.app.user.domain.model.UserDto
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
    private val refreshTokenRepository: RefreshTokenRepository
) {
    data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )

    @OptIn(ExperimentalTime::class)
     suspend fun register(user : User): Pair<UserDto?, String> {
         var phone = user.phone
        if (user.country == "CD"){
            phone = normalizeAndValidatePhoneNumberCD(user.phone) ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Ce numero n'est pas valide."
            )
        }


        val userEntity = userRepository.findByPhoneOrEmail(phone.toString())
        if(userEntity != null) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Cet identifiant existe dans la plateforme.")
        }
        val entity = UserEntity(
            userId = 0,
            password = hashEncoder.encode(user.password),
            typeAccount = mapperAccount.toEntity(user.typeAccount) ,
            email = user.email,
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
        val validIdentifier = normalizeAndValidatePhoneNumber(identifier)
        val user = userRepository.findByPhoneOrEmail(validIdentifier.toString())
            ?: throw BadCredentialsException("Invalid credentials .")

        if(!hashEncoder.matches(password, user.password.toString())) {
            throw BadCredentialsException("Invalid credentials.")
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

    suspend fun changePassword(id : Long,new : String, old : String): UserDto? {
        val data = userRepository.findById(id).orElse(null)
        if(!hashEncoder.matches(old, data.password.toString())) {
            throw BadCredentialsException("Mot de passe invalide.")
        }
        data.password = hashEncoder.encode(new)
        val updatedUser = userRepository.save(data)
        return mapper.toDomain(updatedUser)
    }

    @Transactional
    suspend fun refresh(refreshToken: String): TokenPair {
        if(!jwtService.validateRefreshToken(refreshToken)) {
            throw ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid refresh token.")
        }
        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findById(userId.toLong()).orElseThrow{
            ResponseStatusException(
                HttpStatusCode.valueOf(401),
                "Invalid refresh token."
            )
        }
            val hashed = hashToken(refreshToken)
            refreshTokenRepository.findByUserIdAndHashedToken(user.userId, hashed)
                ?: throw ResponseStatusException(
                    HttpStatusCode.valueOf(401),
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