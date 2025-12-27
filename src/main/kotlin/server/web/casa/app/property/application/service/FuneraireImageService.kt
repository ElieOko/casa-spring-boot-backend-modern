package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.ImageRequestStandard
import server.web.casa.app.property.domain.model.request.ImageChangeRequest
import server.web.casa.app.property.domain.model.request.ImageRequest
import server.web.casa.app.property.infrastructure.persistence.entity.BureauImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFuneraireImageEntity
import server.web.casa.app.property.infrastructure.persistence.repository.BureauImageRepository
import server.web.casa.app.property.infrastructure.persistence.repository.SalleFuneraireImageRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.storage.FileSystemStorageService
import kotlin.collections.forEach

@Service
class FuneraireImageService(
    private val repository: SalleFuneraireImageRepository,
    private val gcsService: GcsService,
    private val storageService: FileSystemStorageService,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val subdirectory = "funeraire/"
   suspend fun create(images: ImageRequestStandard) = coroutineScope {
        val file = base64ToMultipartFile(images.name, "funeraire")
        val imageUri = gcsService.uploadFile(file,subdirectory)
        val data = SalleFuneraireImageEntity(
            salleFuneraireId = images.id,
            name = file.name,
            path = imageUri
        )
        val result = repository.save(data)
       result
    }

    suspend fun updateFile(id: Long, image: List<ImageChangeRequest?>) = coroutineScope {
        val images = repository.findBySalleFuneraireIdIn(listOf(id)).toList()
        val imagesByProperty = images.groupBy { it.salleFuneraireId }
        var state = false
        image.forEach {im->
            log.info("image foreach")
            val ima = imagesByProperty[id]?.firstOrNull { it.name == im?.old }
            log.info("ima ->$ima")
            if (ima != null) {
                val result = gcsService.deleteFile(ima.name,subdirectory)
                log.info("delete $result")
                if (result == true && im?.name != null){
                    log.info("update")
                    val file = base64ToMultipartFile(im.name, "funeraire")
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
    suspend fun deleteFile(id: Long, image: List<ImageRequest?>) = coroutineScope {
        val images =  repository.findBySalleFuneraireIdIn(listOf(id)).toList()
        log.info("image [${images.size}]")
        val imagesByProperty = images.groupBy { it.salleFuneraireId }
        log.info("image by property [${imagesByProperty.size}]")
        var state = false
        image.forEach {im->
            log.info("image foreach")
            val ima = imagesByProperty[id]?.firstOrNull { it.name == im?.name }
            log.info("ima ->$ima")
            if (ima != null) {
                val result = gcsService.deleteFile( ima.name,subdirectory)
                log.info("delete $result")
                if (result == true){
                    repository.delete(ima)
                    log.info("delete")
                    state = true
                }
            }
        }
        state
    }
}