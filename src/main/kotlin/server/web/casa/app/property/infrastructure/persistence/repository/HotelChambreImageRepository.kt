package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.HotelChambreImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFestiveImageEntity

interface HotelChambreImageRepository : CoroutineCrudRepository<HotelChambreImageEntity, Long>{
    fun findByHotelChambreIdIn(hotelChambreIds: List<Long>): Flow<HotelChambreImageEntity>
}