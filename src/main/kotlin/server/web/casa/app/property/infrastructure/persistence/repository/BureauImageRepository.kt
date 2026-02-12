package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.BureauImageEntity

interface BureauImageRepository : CoroutineCrudRepository<BureauImageEntity, Long>{
    fun findByBureauIdIn(bureauIds: List<Long>): Flow<BureauImageEntity>

    @Query("SELECT * FROM bureau_images  WHERE bureau_id = :bureauId")
    fun findAllByBureauId(@Param("bureauId") bureauId: Long): Flow<BureauImageEntity>

}