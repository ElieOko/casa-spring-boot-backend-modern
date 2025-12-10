package server.web.casa.app.ecosystem.application.service.realisation

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import server.web.casa.app.ecosystem.domain.model.realisation.SalubriteRealisation
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.salubrite.ServiceSalubriteRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.salubrite.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.repository.realisation.SalubriteRealisationRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.storage.FileSystemStorageService

@Service
class RealisationSalubriteServiceAction(
    private val repository : SalubriteRealisationRepository,
    private val gcsService: GcsService,
    private val storageService: FileSystemStorageService
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val model = "salubrite"
    suspend fun create(p : SalubriteRealisation,server : String): SalubriteRealisation {
        val file = base64ToMultipartFile(p.name, "${model}_realisation")
        log.info("file ****taille:${file.size}")
        log.info("file ****name:${file.name}")
        val imageUri = gcsService.uploadFile(file,"realisation/$model/")
//        val filename = storageService.store(file, subfolder = "/realisation/$model/")
//        val fileUrl = "$server/realisation/$model/$filename"
//        log.info("public url local $fileUrl")
        p.path = imageUri!!
        val data = ServiceSalubriteRealisationEntity(
            service = p.service.toEntity(),
            name = "",
            path = p.path
        )
        return repository.save(data).toDomain()
    }
}