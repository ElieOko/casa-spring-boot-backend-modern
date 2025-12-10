package server.web.casa.app.ecosystem.application.service.realisation

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import server.web.casa.app.ecosystem.domain.model.realisation.AjusteurRealisation
import server.web.casa.app.ecosystem.domain.model.realisation.ArchitectRealisation
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.ajusteur.ServiceAjusteurRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.ajusteur.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.architect.ServiceArchitectRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.architect.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.repository.realisation.ArchitectRealisationRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.storage.FileSystemStorageService

@Service
class RealisationArchitectServiceAction(
    private val repository : ArchitectRealisationRepository,
    private val gcsService: GcsService,
    private val storageService: FileSystemStorageService
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val model = "architect"
    suspend fun create(p : ArchitectRealisation,server : String): ArchitectRealisation {
        val file = base64ToMultipartFile(p.name, "${model}_realisation")
        log.info("file ****taille:${file.size}")
        log.info("file ****name:${file.name}")
        val imageUri = gcsService.uploadFile(file,"realisation/$model/")
//        val filename = storageService.store(file, subfolder = "/realisation/$model/")
//        val fileUrl = "$server/realisation/$model/$filename"
//        log.info("public url local $fileUrl")
        p.path = imageUri!!
        val data = ServiceArchitectRealisationEntity(
            service = p.service.toEntity(),
            name = "",
            path = p.path
        )
        return repository.save(data).toDomain()
    }
}