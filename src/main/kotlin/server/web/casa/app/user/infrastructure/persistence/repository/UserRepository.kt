package server.web.casa.app.user.infrastructure.persistence.repository

import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import reactor.core.publisher.Mono
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity

interface UserRepository : CoroutineCrudRepository<UserEntity, Long> {

    @Query("SELECT * FROM users WHERE email = :identifier OR phone = :identifier AND is_lock = false")
   suspend fun findByPhoneOrEmail(identifier: String) : UserEntity?
//    suspend fun findByEmailOrPhone(email: String, phone: String): Mono<UserEntity>?

    @Query("SELECT * FROM users WHERE is_lock = false")
    override suspend fun findById(id: Long): UserEntity?
    @Modifying
    @Query("""UPDATE users
    SET is_lock = :lock
    WHERE user_id = :userId"""
    )
    suspend fun isLock(userId: Long, lock: Boolean = true): Int
}