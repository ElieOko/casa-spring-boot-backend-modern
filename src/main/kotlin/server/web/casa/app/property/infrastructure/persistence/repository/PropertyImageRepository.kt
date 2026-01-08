package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageKitchenEntity

interface PropertyImageRepository : CoroutineCrudRepository<PropertyImageEntity, Long>{
    fun findByPropertyIdIn(propertyIds: List<Long>): Flow<PropertyImageEntity>
}