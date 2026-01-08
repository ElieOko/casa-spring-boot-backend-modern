package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.HotelChambre
import server.web.casa.app.property.domain.model.dto.HotelChambreDTO
import server.web.casa.app.property.domain.model.toEntity
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.repository.HotelChambreImageRepository
import server.web.casa.app.property.infrastructure.persistence.repository.HotelChambreRepository
import kotlin.collections.map

@Service
class HotelChambreService(
    private val repository : HotelChambreRepository,
    private val images : HotelChambreImageRepository,
) {
    suspend fun create(model : HotelChambre) = coroutineScope { repository.save(model.toEntity()) }

    suspend fun update(p: HotelChambre) = coroutineScope { repository.save(p.toEntity()).toDomain() }

    suspend fun getAllChambreByHotel(hotel : Long) = coroutineScope{
        val data = repository.getAllByHotel(hotel).toList()
        val hotelList = mutableListOf<HotelChambreDTO>()
        val hotelIds: List<Long> = data.map { it?.id!! }
        val images = images.findByHotelChambreIdIn(hotelIds).toList()
        val imageByBureau = images.groupBy { it.hotelChambreId }
        data.forEach { ag->
            hotelList.add(
                HotelChambreDTO(
                    chambre = ag!!.toDomain(),
                    image = imageByBureau[ag.id]?.map { it.toDomain() } ?: emptyList()
                )
            )
        }
        hotelList
    }

    suspend fun getAll() = coroutineScope { repository.findAll().toList() }
}