package server.web.casa.app.ecosystem.application.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import server.web.casa.app.ecosystem.domain.model.PrestationImage
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.PrestationImageEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.repository.PrestationImageRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.storage.FileSystemStorageService

@Service
class PrestationImageService(
    private val repository: PrestationImageRepository,
    private val gcsService: GcsService,
    private val storageService: FileSystemStorageService
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val model = "electricien"
    suspend fun create(p : PrestationImage,server : String): PrestationImage {
        val file = base64ToMultipartFile(p.name, "${model}_realisation")
        log.info("file ****taille:${file.size}")
        log.info("file ****name:${file.name}")
        val imageUri = gcsService.uploadFile(file,"realisation/$model/")
//        val filename = storageService.store(file, subfolder = "/realisation/$model/")
//        val fileUrl = "$server/realisation/$model/$filename"
//        log.info("public url local $fileUrl")
        p.path = imageUri!!
        val data = PrestationImageEntity(
            prestationId = p.prestationId,
            name = "",
            path = p.path
        )
        return repository.save(data).toDomain()
    }
}