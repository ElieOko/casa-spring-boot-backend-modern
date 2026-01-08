package server.web.casa.app.user.infrastructure.persistence.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.user.infrastructure.persistence.entity.RefreshToken

interface RefreshTokenRepository : CoroutineCrudRepository<RefreshToken, Long> {
    suspend fun findByUserIdAndHashedToken(userId: Long, hashedToken: String): RefreshToken?
    suspend fun deleteByUserIdAndHashedToken(userId: Long, hashedToken: String)
}