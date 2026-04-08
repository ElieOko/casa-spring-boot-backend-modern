package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.actor.infrastructure.persistence.repository.PersonRepository
import server.web.casa.app.property.domain.model.Hotel
import server.web.casa.app.property.domain.model.ImageRequestStandard
import server.web.casa.app.property.domain.model.dto.HotelGlobal
import server.web.casa.app.property.domain.model.toDomain
import server.web.casa.app.property.domain.model.toEntity
import server.web.casa.app.property.infrastructure.persistence.entity.BureauImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.HotelImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.mapper.toDomain
import server.web.casa.app.property.infrastructure.persistence.repository.HotelImageRepository
import server.web.casa.app.property.infrastructure.persistence.repository.HotelRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import kotlin.collections.map

@Service
class HotelService(
    private val hotelRepository: HotelRepository,
    private val imagesHotel : HotelImageRepository,
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
    suspend fun findByNoRestrict(id : Long) = coroutineScope {
        hotelRepository.findByIdNoRestrict(id)?.toDomain()?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette hotel n'existe.")
    }

    suspend fun createFile(images: ImageRequestStandard) = coroutineScope {
        val file = base64ToMultipartFile(images.name, "hotel")
        val imageUri = gcsService.uploadFile(file,"hotel/")
        val data = HotelImageEntity(
            hotelId = images.id,
            name = file.name,
            path = imageUri
        )
        val result = imagesHotel.save(data)
        result
    }

    suspend fun getAllHotel(state: Boolean = false) = coroutineScope{
        val hotel = mutableListOf<HotelGlobal>()
        val data = if (!state) hotelRepository.findAll().toList() else hotelRepository.findAllData().toList()
        val hotelIds: List<Long> = data.map { it.id!! }
        val images = imagesHotel.findByHotelIdIn(hotelIds).toList()
        val imageByHotel = images.groupBy { it.hotelId }
        data.forEach {h->
            hotel.add(
                HotelGlobal(
                    images = imageByHotel[h.id]?.map { it.toDomain() } ?: emptyList(),
                    hotel = h.toDomain(),
                    image = person.findByUser(h.userId!!)?.images?:"",
                    structure =  chambre.getAllChambreByHotel(h.id?:0),
                )
            )
        }
        hotel.toList()
    }

    suspend fun getAllByUser(userId : Long) = coroutineScope{
        val data = hotelRepository.getAllByUser(userId).toList()
        val hotel = mutableListOf<HotelGlobal>()
        val hotelIds = data.map { it?.id!! }
        val images = imagesHotel.findByHotelIdIn(hotelIds).toList()
        val imageByHotel = images.groupBy { it.hotelId }
        data.map {it?.toDomain()}.toList().forEach { hotel.add(
            HotelGlobal(
                images = imageByHotel[it?.id]?.map { it.toDomain() } ?: emptyList(),
                hotel = it!!,
                structure =  chambre.getAllChambreByHotel(it.id?:0),
                image = person.findByUser(it.userId!!)?.images?:"",
            )
        ) }
        hotel.toList()
    }

    suspend fun showDetail(id : Long, state: Boolean = true) = coroutineScope {
        val data = (if (state) hotelRepository.findById(id) else hotelRepository.findByIdNoRestrict(id) )?:throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette hotel n'existe.")
        val hotel = mutableListOf<HotelGlobal>()
        val hotelIds = listOf(data.id!!)
        val images = imagesHotel.findByHotelIdIn(hotelIds).toList()
        val imageByHotel = images.groupBy { it.hotelId }
        hotel.add(
            HotelGlobal(
                images = imageByHotel[data.id]?.map { it.toDomain() } ?: emptyList(),
                hotel = data.toDomain(),
                structure =  chambre.getAllChambreByHotel(data.id?:0),
                image = person.findByUser(data.userId!!)?.images?:"",
            )
        )
        hotel.toList()
    }

    suspend fun findById(id : Long) = coroutineScope {
        hotelRepository.findById(id)?.toDomain()?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette proprièté n'existe pas de salle funeraire.")
    }
}