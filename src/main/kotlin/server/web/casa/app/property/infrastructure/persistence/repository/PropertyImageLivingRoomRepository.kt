package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageKitchenEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageLivingRoomEntity

interface PropertyImageLivingRoomRepository : CoroutineCrudRepository<PropertyImageLivingRoomEntity, Long>{
    fun findByPropertyIdIn(propertyIds: List<Long>): Flow<PropertyImageLivingRoomEntity>
}