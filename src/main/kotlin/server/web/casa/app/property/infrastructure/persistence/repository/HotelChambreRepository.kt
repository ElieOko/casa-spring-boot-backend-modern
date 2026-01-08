package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.HotelChambreEntity
import server.web.casa.app.property.infrastructure.persistence.entity.VacanceEntity

interface HotelChambreRepository : CoroutineCrudRepository<HotelChambreEntity, Long>{
    @Query("SELECT * FROM hotel_chambres WHERE hotel_id = :hotelId")
    suspend fun getAllByHotel(hotelId : Long) : Flow<HotelChambreEntity?>
}