package server.web.casa.app.prestation.infrastructure.persistance.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.prestation.infrastructure.persistance.entity.SollicitationEntity

interface SollicitationRepository : CoroutineCrudRepository<SollicitationEntity, Long> {
    @Query("SELECT * FROM sollicitations WHERE user_id = :userId")
    fun findByUserId(@Param("userId") userId: Long): Flow<SollicitationEntity>

    @Query("SELECT * FROM sollicitations WHERE prestation_id = :prestationId")
    fun findByPrestationId(@Param("prestationId") prestationId: Long): Flow<SollicitationEntity>

    @Query("""
        SELECT r.* 
        FROM sollicitations r
        JOIN prestations p ON r.prestation_id = p.id
        WHERE p.user_id = :userId
    """)
    fun findByHostUserId(@Param("userId") userId: Long): Flow<SollicitationEntity>

    @Query("SELECT * FROM sollicitations WHERE user_id = :userId AND prestation_id = :prestationId")
    fun findByUserIdPrestationId(@Param("userId") userId: Long, @Param("prestationId") prestationId: Long): Flow<SollicitationEntity>

}