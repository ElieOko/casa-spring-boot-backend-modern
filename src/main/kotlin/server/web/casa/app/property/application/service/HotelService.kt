package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.actor.application.service.PersonService
import server.web.casa.app.property.domain.model.Hotel
import server.web.casa.app.property.domain.model.dto.HotelGlobal
import server.web.casa.app.property.domain.model.toEntity
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.repository.HotelRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService

@Service
class HotelService(
    private val hotelRepository: HotelRepository,
    private val gcsService: GcsService,
    private val person : PersonService,
    private val chambre : HotelChambreService
) {
    suspend fun save(domain : Hotel) = coroutineScope {
        try {
            val file = base64ToMultipartFile(domain.image, "hotel")
            val imageUri = gcsService.uploadFile(file,"hotel/")
            domain.image = imageUri!!
            hotelRepository.save(domain.toEntity())
        } catch (e: Exception) {
            throw Exception(e.message, e)
        }
    }
//    suspend fun getAll() = coroutineScope { hotelRepository.findAll().map { it.toDomain() }.toList() }
    suspend fun getAllHotel() = coroutineScope{
        val hotel = mutableListOf<HotelGlobal>()
        hotelRepository.findAll().collect {
            hotel.add(
                HotelGlobal(
                    hotel = it.toDomain(),
                    image = person.findByIdPersonUser(it.userId!!)?.images?:"",
                    structure =  chambre.getAllChambreByHotel(it.id?:0),
                )
            )
        }
        hotel.toList()
    }

    private suspend fun findAll() = coroutineScope {

    }
    suspend fun getAllByUser(userId : Long) = coroutineScope{
        val data = hotelRepository.getAllByUser(userId)
        val hotel = mutableListOf<HotelGlobal>()
        data.map {it?.toDomain()}.toList().forEach { hotel.add(
            HotelGlobal(
                hotel = it!!,
                image = person.findByIdPersonUser(it.userId!!)?.images?:"",
                structure =  chambre.getAllChambreByHotel(it.id?:0)
            )
        ) }
        hotel.toList()
    }
}