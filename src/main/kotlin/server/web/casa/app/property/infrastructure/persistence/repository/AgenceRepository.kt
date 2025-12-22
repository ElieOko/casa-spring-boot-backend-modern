package server.web.casa.app.property.infrastructure.persistence.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.AgenceEntity

interface AgenceRepository : CoroutineCrudRepository<AgenceEntity, Long>{
    @Query("SELECT COUNT(*) FROM agences WHERE user_id = :userId")
    suspend fun countByUserId(userId: Long) : Long
}