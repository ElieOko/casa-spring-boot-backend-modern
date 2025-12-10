package server.web.casa.app.ecosystem.application.service.realisation

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import server.web.casa.app.ecosystem.domain.model.realisation.PeintreRealisation
import server.web.casa.app.ecosystem.domain.model.realisation.PlombierRealisation
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.peintre.ServicePeintreRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.peintre.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.plombier.ServicePlombierRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.plombier.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.repository.realisation.PlombierRealisationRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.storage.FileSystemStorageService

@Service
class RealisationPlombierServiceAction(
    private val repository : PlombierRealisationRepository,
    private val gcsService: GcsService,
    private val storageService: FileSystemStorageService
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val model = "plombier"
    suspend fun create(p : PlombierRealisation,server : String): PlombierRealisation {
        val file = base64ToMultipartFile(p.name, "${model}_realisation")
        log.info("file ****taille:${file.size}")
        log.info("file ****name:${file.name}")
        val imageUri = gcsService.uploadFile(file,"realisation/$model/")
//        val filename = storageService.store(file, subfolder = "/realisation/$model/")
//        val fileUrl = "$server/realisation/$model/$filename"
//        log.info("public url local $fileUrl")
        p.path = imageUri!!
        val data = ServicePlombierRealisationEntity(
            service = p.service.toEntity(),
            name = "",
            path = p.path
        )
        return repository.save(data).toDomain()
    }
}