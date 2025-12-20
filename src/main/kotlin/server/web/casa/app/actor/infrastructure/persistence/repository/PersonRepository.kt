package server.web.casa.app.actor.infrastructure.persistence.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.actor.infrastructure.persistence.entity.PersonEntity
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity

interface PersonRepository : CoroutineCrudRepository<PersonEntity, Long>{
    @Query("SELECT * FROM persons WHERE user_id = :userId")
    suspend fun findByUser(userId: Long) : PersonEntity?
}