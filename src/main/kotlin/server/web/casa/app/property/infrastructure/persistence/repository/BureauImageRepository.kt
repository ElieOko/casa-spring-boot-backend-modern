package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.BureauImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyFeatureEntity
import server.web.casa.app.property.infrastructure.persistence.entity.VacanceEntity
import server.web.casa.app.property.infrastructure.persistence.entity.VacanceImageEntity

interface BureauImageRepository : CoroutineCrudRepository<BureauImageEntity, Long>{
    fun findByBureauIdIn(bureauIds: List<Long>): Flow<BureauImageEntity>

}