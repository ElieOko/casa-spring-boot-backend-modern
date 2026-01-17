package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.PropertyImage
import server.web.casa.app.property.domain.model.request.*
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageEntity
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyImageRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import kotlin.collections.groupBy

@Service
class PropertyImageService(
    private val repository: PropertyImageRepository,
    private val gcsService: GcsService
) {
    private val subdirectory = "property/images/"
    suspend fun create(p : PropertyImage,server : String): PropertyImageEntity {
        val file = base64ToMultipartFile(p.name, "property")
        val imageUri = gcsService.uploadFile(file,subdirectory)
//        val filename = storageService.store(file, subfolder = "/property/images/")
//        val fileUrl = "$server/property/images/$filename"
//        log.info("file uri ****name:${imageUri}")
        p.path = imageUri!!
        p.name = file.originalFilename!!
        val data = PropertyImageEntity(
            propertyId = p.propertyId,
            name = p.name,
            path = p.path
        )
        val result = repository.save(data)
        return result
    }
    suspend fun findPropertyIdIn(ids : List<Long>) = repository.findByPropertyIdIn(ids)
    suspend fun updateFile(propertyId: Long, image: List<ImageChangeRequest?>) = coroutineScope {
        val images = findPropertyIdIn(listOf(propertyId)).toList()
        val imagesByProperty = images.groupBy { it.propertyId }
        var state = false
        image.forEach {im->
            val ima = imagesByProperty[propertyId]?.firstOrNull { it.name == im?.old }
            if (ima != null) {
                val result = gcsService.deleteFile(ima.name,subdirectory)
                if (result == true && im?.name != null){
                    val file = base64ToMultipartFile(im.name, "property")
                    val imageUri = gcsService.uploadFile(file,subdirectory)
                    ima.path = imageUri!!
                    ima.name = file.originalFilename!!
                    repository.save(ima)
                    state = true
                }
            }
        }
        state
    }
    suspend fun deleteFile(propertyId: Long, image: List<ImageRequest?>) = coroutineScope {
        val images = findPropertyIdIn(listOf(propertyId)).toList()
        val imagesByProperty = images.groupBy { it.propertyId }
        var state = false
        image.forEach {im->
           val ima = imagesByProperty[propertyId]?.firstOrNull { it.name == im?.name }
           if (ima != null) {
             val result = gcsService.deleteFile( ima.name,subdirectory)
             if (result == true){
                 repository.delete(ima)
                 state = true
             }
           }
        }
        state
    }
//    fun getAll() : List<PropertyImage> = repository.findAll().stream().map { mapper.toDomain(it) }.toList()
}