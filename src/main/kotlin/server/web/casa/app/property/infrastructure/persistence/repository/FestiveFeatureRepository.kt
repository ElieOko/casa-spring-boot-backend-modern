package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.BureauFeatureEntity
import server.web.casa.app.property.infrastructure.persistence.entity.FestiveFeatureEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyFeatureEntity

interface FestiveFeatureRepository : CoroutineCrudRepository<FestiveFeatureEntity, Long>{
    fun findByFestiveIdIn(festiveIds: List<Long>): Flow<FestiveFeatureEntity>
}
