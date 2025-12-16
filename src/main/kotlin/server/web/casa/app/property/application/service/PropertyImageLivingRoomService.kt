package server.web.casa.app.property.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.PropertyImageLivingRoom
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageLivingRoomEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.toEntity
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyImageLivingRoomRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.storage.FileSystemStorageService

@Service
class PropertyImageLivingRoomService(
    private val repository: PropertyImageLivingRoomRepository,
    private val gcsService: GcsService,
    private val storageService: FileSystemStorageService
) {
    suspend fun create(p : PropertyImageLivingRoom, server : String): PropertyImageLivingRoomEntity {
        val file = base64ToMultipartFile(p.name,"living_room/")
        val imageUri = gcsService.uploadFile(file,"property/living/")
        p.path = imageUri!!
//        p.name = file.name
        val filename = storageService.store(file, subfolder = "/property/living/")
        val fileUrl = "$server/property/living/$filename"
        val data = PropertyImageLivingRoomEntity(
            propertyLivingId = p.property!!.propertyId,
            name = fileUrl,
            path = p.path
        )
        val result = repository.save(data)
        return result
    }
//    fun getAll() : List<PropertyImageLivingRoom> = repository.findAll().stream().map { mapper.toDomain(it) }.toList()
}