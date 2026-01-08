package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyFeatureEntity

interface PropertyFeatureRepository : CoroutineCrudRepository<PropertyFeatureEntity, Long>{
    fun findByPropertyIdIn(propertyIds: List<Long>): Flow<PropertyFeatureEntity>
}
