package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.BureauFeatureEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyFeatureEntity

interface BureauFeatureRepository : CoroutineCrudRepository<BureauFeatureEntity, Long>{
    fun findByBureauIdIn(bureauIds: List<Long>): Flow<BureauFeatureEntity>
}
