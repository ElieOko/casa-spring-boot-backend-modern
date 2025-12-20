package server.web.casa.app.ecosystem.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.PrestationEntity
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity

interface PrestationRepository : CoroutineCrudRepository<PrestationEntity, Long>{

    @Query("SELECT * FROM prestations WHERE user_id = :userId AND service_id = :serviceId")
    suspend fun findByUserAndService(userId: Long, serviceId : Long) : PrestationEntity?

    @Query("SELECT COUNT(*) FROM prestations WHERE user_id = :userId")
    suspend fun countByUserId(userId: Long) : Long

    @Query("SELECT * FROM prestations WHERE is_active = true and is_certified = true")
    suspend fun findAllFilter() : Flow<PrestationEntity>
}
