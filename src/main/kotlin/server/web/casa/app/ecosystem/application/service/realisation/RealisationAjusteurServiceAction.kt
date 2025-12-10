package server.web.casa.app.ecosystem.application.service.realisation

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import server.web.casa.app.ecosystem.domain.model.realisation.AjusteurRealisation
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.ajusteur.ServiceAjusteurRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.ajusteur.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.repository.realisation.AjusteurRealisationRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.storage.FileSystemStorageService

@Service
class RealisationAjusteurServiceAction(
    private val repository : AjusteurRealisationRepository,
    private val gcsService: GcsService,
    private val storageService: FileSystemStorageService
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    suspend fun create(p : AjusteurRealisation,server : String): AjusteurRealisation {
        val file = base64ToMultipartFile(p.name, "ajusteur_realisation")
        log.info("file ****taille:${file.size}")
        log.info("file ****name:${file.name}")
        val imageUri = gcsService.uploadFile(file,"realisation/ajusteur/")
//        val filename = storageService.store(file, subfolder = "/realisation/ajusteur/")
//        val fileUrl = "$server/realisation/ajusteur/$filename"
//        log.info("public url local $fileUrl")
        p.path = imageUri!!
        val data = ServiceAjusteurRealisationEntity(
            service = p.service.toEntity(),
            name = "",
            path = p.path
        )
        return repository.save(data).toDomain()
    }
}