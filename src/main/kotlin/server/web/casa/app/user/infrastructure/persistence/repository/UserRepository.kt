package server.web.casa.app.user.infrastructure.persistence.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import reactor.core.publisher.Mono
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity

interface UserRepository : CoroutineCrudRepository<UserEntity, Long> {

    @Query("SELECT * FROM users WHERE email = :identifier OR phone = :identifier")
   suspend fun findByPhoneOrEmail(identifier: String):UserEntity?
//    suspend fun findByEmailOrPhone(email: String, phone: String): Mono<UserEntity>?


}