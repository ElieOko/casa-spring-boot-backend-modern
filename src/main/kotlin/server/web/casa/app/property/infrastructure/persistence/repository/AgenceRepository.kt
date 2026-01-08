package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.AgenceEntity

interface AgenceRepository : CoroutineCrudRepository<AgenceEntity, Long>{
    @Query("SELECT COUNT(*) FROM agencies WHERE user_id = :userId")
    suspend fun countByUserId(userId: Long) : Long

    @Query("SELECT * FROM agencies WHERE user_id = :userId")
    suspend fun getAllByUser(userId: Long) : Flow<AgenceEntity?>
}