package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.HotelImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageKitchenEntity

interface HotelImageRepository : CoroutineCrudRepository<HotelImageEntity, Long>{
    fun findByHotelIdIn(hotelIds: List<Long>): Flow<HotelImageEntity>
}