package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.HotelChambreImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFestiveImageEntity

interface HotelChambreImageRepository : CoroutineCrudRepository<HotelChambreImageEntity, Long>{
    fun findByHotelChambreIdIn(hotelChambreIds: List<Long>): Flow<HotelChambreImageEntity>

    @Query("SELECT * FROM hotel_chambre_images  WHERE hotel_chambre_id = :chambreId")
    fun findAllByChambreID(@Param("chambreId") chambreId: Long): Flow<HotelChambreImageEntity>

}