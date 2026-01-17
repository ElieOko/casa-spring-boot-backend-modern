package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.PropertyImageLivingRoom
import server.web.casa.app.property.domain.model.request.*
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageLivingRoomEntity
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyImageLivingRoomRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import kotlin.collections.forEach

@Service
class PropertyImageLivingRoomService(
    private val repository: PropertyImageLivingRoomRepository,
    private val gcsService: GcsService
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val subdirectory = "property/living/"
    suspend fun create(p : PropertyImageLivingRoom, server : String): PropertyImageLivingRoomEntity {
        val file = base64ToMultipartFile(p.name,"living_room")
        val imageUri = gcsService.uploadFile(file,subdirectory)
        p.path = imageUri!!
        p.name = file.originalFilename!!
//        val filename = storageService.store(file, subfolder = "/property/living/")
//        val fileUrl = "$server/property/living/$filename"
        val data = PropertyImageLivingRoomEntity(
            propertyId = p.propertyId!!,
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
            log.info("image foreach")
            val ima = imagesByProperty[propertyId]?.firstOrNull { it.name == im?.old }
            log.info("ima ->$ima")
            if (ima != null) {
                val result = gcsService.deleteFile(ima.name,subdirectory)
                log.info("delete $result")
                if (result == true && im?.name != null){
                    log.info("update")
                    val file = base64ToMultipartFile(im.name, "living_room")
                    val imageUri = gcsService.uploadFile(file,subdirectory)
                    ima.path = imageUri!!
                    ima.name = file.originalFilename!!
                    repository.save(ima)
                    log.info("change image")
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
        image.forEach { im ->
            val ima = imagesByProperty[propertyId]?.firstOrNull{ it.name == im?.name }
            if (ima != null) {
                val result = gcsService.deleteFile( ima.name,subdirectory)
                if (result == true){
                    repository.delete(ima)
                    log.info("delete")
                    state = true
                }
            }
        }
        state
    }
//    fun getAll() : List<PropertyImageLivingRoom> = repository.findAll().stream().map { mapper.toDomain(it) }.toList()
}