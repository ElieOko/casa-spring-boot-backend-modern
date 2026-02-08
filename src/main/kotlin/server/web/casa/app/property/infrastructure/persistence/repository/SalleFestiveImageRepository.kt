package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFestiveImageEntity

interface SalleFestiveImageRepository : CoroutineCrudRepository<SalleFestiveImageEntity, Long>{
    fun findBySalleFestiveIdIn(salleFestiveIds: List<Long>): Flow<SalleFestiveImageEntity>

    @Query("SELECT * FROM salle_festive_images  WHERE salle_festive_id = :salleId")
    fun findAllBySalleId(@Param("salleId") salleId: Long): Flow<SalleFestiveImageEntity>

}