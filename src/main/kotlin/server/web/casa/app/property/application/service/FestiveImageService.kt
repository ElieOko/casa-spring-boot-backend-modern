package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.ImageRequestStandard
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFestiveImageEntity
import server.web.casa.app.property.infrastructure.persistence.repository.SalleFestiveImageRepository
import server.web.casa.app.user.application.service.UserService
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.storage.FileSystemStorageService

@Service
class FestiveImageService(
    private val repository: SalleFestiveImageRepository,
    private val gcsService: GcsService,
    private val storageService: FileSystemStorageService,
    private val userService: UserService,
) {
   suspend fun create(images: ImageRequestStandard) = coroutineScope {
        val file = base64ToMultipartFile(images.name, "festive")
        val imageUri = gcsService.uploadFile(file,"festive/")
        val data = SalleFestiveImageEntity(
            salleFestiveId = images.id,
            name = "",
            path = imageUri
        )
        val result = repository.save(data)
       result
    }
}