package server.web.casa.app.property.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.PropertyImageRoom
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageRoomEntity
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyImageRoomRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.storage.FileSystemStorageService

@Service
class PropertyImageRoomService(
    private val repository: PropertyImageRoomRepository,
    private val gcsService: GcsService,
    private val storageService: FileSystemStorageService
) {
    suspend fun create(p : PropertyImageRoom, server : String): PropertyImageRoomEntity {
        val file = base64ToMultipartFile(p.name,"room")
        val imageUri = gcsService.uploadFile(file,"property/room/")
        p.path = imageUri!!
//        p.name = file.name
//        val filename = storageService.store(file, subfolder = "/property/room/")
//        val fileUrl = "$server/property/room/$filename"
        val data = PropertyImageRoomEntity(
            propertyId = p.propertyId,
            name = "",
            path = p.path
        )
        val result = repository.save(data)
        return result
    }
    suspend fun findPropertyIdIn(ids : List<Long>) = repository.findByPropertyIdIn(ids)
//    fun getAll() : List<PropertyImageRoom> = repository.findAll().stream().map { mapper.toDomain(it) }.toList()
}