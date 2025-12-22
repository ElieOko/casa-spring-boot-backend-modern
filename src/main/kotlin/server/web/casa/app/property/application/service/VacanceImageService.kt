package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.ImageRequestStandard
import server.web.casa.app.property.infrastructure.persistence.entity.VacanceImageEntity
import server.web.casa.app.property.infrastructure.persistence.repository.VacanceImageRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.storage.FileSystemStorageService

@Service
class VacanceImageService(
    private val repository: VacanceImageRepository,
    private val gcsService: GcsService,
    private val storageService: FileSystemStorageService
) {
   suspend fun create(images: ImageRequestStandard) = coroutineScope {
        val file = base64ToMultipartFile(images.name, "vacance")
        val imageUri = gcsService.uploadFile(file,"vacance/")
        val data = VacanceImageEntity(
            vacanceId = images.id,
            name = "",
            path = imageUri
        )
        val result = repository.save(data)
       result
    }
}