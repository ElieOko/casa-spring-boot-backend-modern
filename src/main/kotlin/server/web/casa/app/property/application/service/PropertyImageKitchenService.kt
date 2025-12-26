package server.web.casa.app.property.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.PropertyImageKitchen
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageKitchenEntity
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyImageKitchenRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.storage.FileSystemStorageService

@Service
class PropertyImageKitchenService(
    private val repository: PropertyImageKitchenRepository,
    private val gcsService: GcsService,
    private val storageService: FileSystemStorageService
) {
    suspend fun create(p : PropertyImageKitchen, server : String): PropertyImageKitchenEntity {
        val file = base64ToMultipartFile(p.name,"kitchen")
        val imageUri = gcsService.uploadFile(file,"property/kitchen/")
        p.path = imageUri!!
//        p.name = file.name
//        val filename = storageService.store(file, subfolder = "/property/kitchen/")
//        val fileUrl = "$server/property/kitchen/$filename"
        val data = PropertyImageKitchenEntity(
            propertyId = p.propertyId,
            name = p.name,
            path = p.path
        )
        val result = repository.save(data)
        return result
    }
    suspend fun findPropertyIdIn(ids : List<Long>) = repository.findByPropertyIdIn(ids)

//    fun getAll() : List<PropertyImageKitchen> = repository.findAll().stream().map { mapper.toDomain(it) }.toList()
}