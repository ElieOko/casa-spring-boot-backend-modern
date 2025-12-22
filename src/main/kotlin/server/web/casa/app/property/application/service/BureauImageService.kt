package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.ImageRequestStandard
import server.web.casa.app.property.infrastructure.persistence.entity.BureauImageEntity
import server.web.casa.app.property.infrastructure.persistence.repository.BureauImageRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.storage.FileSystemStorageService

@Service
class BureauImageService(
    private val repository: BureauImageRepository,
    private val gcsService: GcsService,
    private val storageService: FileSystemStorageService
) {
   suspend fun create(images: ImageRequestStandard) = coroutineScope {
        val file = base64ToMultipartFile(images.name, "bureau")
        val imageUri = gcsService.uploadFile(file,"bureau/")
        val data = BureauImageEntity(
            bureauId = images.id,
            name = "",
            path = imageUri
        )
        val result = repository.save(data)
       result
    }
}