package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.VacanceImageEntity

interface VacanceImageRepository : CoroutineCrudRepository<VacanceImageEntity, Long>{
    fun findByVacanceIdIn(vacanceIds: List<Long>): Flow<VacanceImageEntity>

    @Query("SELECT * FROM vacance_images WHERE vacance_id = :vacanceID")
    fun findByVacanceID(@Param("vacanceID") vacanceID: Long): Flow<VacanceImageEntity>

}