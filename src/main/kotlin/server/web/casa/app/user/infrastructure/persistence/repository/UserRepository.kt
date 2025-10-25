package server.web.casa.app.user.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity

interface UserRepository : CoroutineCrudRepository<UserEntity, Long> {

    @Query("SELECT r FROM UserEntity r WHERE r.email = :identifier OR r.phone = :identifier")
    suspend fun findByPhoneOrEmail(@Param("identifier") identifier: String): UserEntity?

}