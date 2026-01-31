package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.ImageRequestStandard
import server.web.casa.app.property.domain.model.request.*
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFestiveImageEntity
import server.web.casa.app.property.infrastructure.persistence.repository.SalleFestiveImageRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import kotlin.collections.forEach

@Service
class FestiveImageService(
    private val repository: SalleFestiveImageRepository,
    private val gcsService: GcsService
) {
    private val subdirectory = "festive/"
   suspend fun create(images: ImageRequestStandard) = coroutineScope {
        val file = base64ToMultipartFile(images.name, "festive")
        val imageUri = gcsService.uploadFile(file,"festive/")
        val data = SalleFestiveImageEntity(
            salleFestiveId = images.id,
            name = file.name,
            path = imageUri
        )
        val result = repository.save(data)
       result
    }
    suspend fun updateFile(id: Long, image: List<ImageChangeRequest?>) = coroutineScope {
        val images = repository.findBySalleFestiveIdIn(listOf(id)).toList()
        val imagesByProperty = images.groupBy { it.salleFestiveId }
        var state = false
        image.forEach {im->
            val ima = imagesByProperty[id]?.firstOrNull { it.name == im?.old }
            if (ima != null) {
                val result = gcsService.deleteFile(ima.name,subdirectory)
                if (result == true && im?.name != null){
                    val file = base64ToMultipartFile(im.name, "festive")
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
    suspend fun deleteFile(id: Long, image: List<ImageRequest?>) = coroutineScope {
        val images =  repository.findBySalleFestiveIdIn(listOf(id)).toList()
        val imagesByProperty = images.groupBy { it.salleFestiveId }
        var state = false
        image.forEach {im->
            val ima = imagesByProperty[id]?.firstOrNull { it.name == im?.name }
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
}