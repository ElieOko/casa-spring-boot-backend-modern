package server.web.casa.app.prestation.infrastructure.persistance.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.prestation.infrastructure.persistance.entity.FavoritePrestationEntity
import server.web.casa.app.prestation.infrastructure.persistance.entity.SollicitationEntity

interface SollicitationRepository : CoroutineCrudRepository<SollicitationEntity, Long> {
    @Query("SELECT * FROM sollicitations WHERE user_id = :userId")
    fun findByUserId(@Param("userId") userId: Long): Flow<SollicitationEntity>?
}