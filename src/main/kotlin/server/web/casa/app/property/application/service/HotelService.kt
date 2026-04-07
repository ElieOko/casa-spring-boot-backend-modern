package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.actor.infrastructure.persistence.repository.PersonRepository
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
    private val person : PersonRepository,
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
    suspend fun getAllHotel(state: Boolean = false) = coroutineScope{
        val hotel = mutableListOf<HotelGlobal>()
        val flow = if (!state) hotelRepository.findAll() else hotelRepository.findAllData()
        flow.collect {
            hotel.add(
                HotelGlobal(
                    hotel = it.toDomain(),
                    image = person.findByUser(it.userId!!)?.images?:"",
                    structure =  chambre.getAllChambreByHotel(it.id?:0),
                )
            )
        }
        hotel.toList()
    }

    suspend fun getAllByUser(userId : Long) = coroutineScope{
        val data = hotelRepository.getAllByUser(userId)
        val hotel = mutableListOf<HotelGlobal>()
        data.map {it?.toDomain()}.toList().forEach { hotel.add(
            HotelGlobal(
                hotel = it!!,
                image = person.findByUser(it.userId!!)?.images?:"",
                structure =  chambre.getAllChambreByHotel(it.id?:0)
            )
        ) }
        hotel.toList()
    }

    suspend fun showDetail(id : Long) = coroutineScope{
        val data = hotelRepository.findById(id)?:throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette hotel n'existe.")
        val hotel = mutableListOf<HotelGlobal>()
        hotel.add(
            HotelGlobal(
                hotel = data.toDomain(),
                image = person.findByUser(data.userId!!)?.images?:"",
                structure =  chambre.getAllChambreByHotel(data.id?:0)
            )
        )
        hotel.toList()
    }
    suspend fun findById(id : Long) = coroutineScope {
        hotelRepository.findById(id)?.toDomain()?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette proprièté n'existe pas de salle funeraire.")
    }
}