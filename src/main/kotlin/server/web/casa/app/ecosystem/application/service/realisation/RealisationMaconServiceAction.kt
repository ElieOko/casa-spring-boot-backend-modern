package server.web.casa.app.ecosystem.application.service.realisation

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import server.web.casa.app.ecosystem.domain.model.realisation.FrigoristeRealisation
import server.web.casa.app.ecosystem.domain.model.realisation.MaconRealisation
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.frigoriste.ServiceFrigoristeRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.frigoriste.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.macon.ServiceMaconRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.macon.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.repository.realisation.FrigoristeRealisationRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.repository.realisation.MaconRealisationRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.storage.FileSystemStorageService

@Service
class RealisationMaconServiceAction(
    private val repository: MaconRealisationRepository,
    private val gcsService: GcsService,
    private val storageService: FileSystemStorageService
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val model = "macon"
    suspend fun create(p : MaconRealisation,server : String): MaconRealisation {
        val file = base64ToMultipartFile(p.name, "${model}_realisation")
        log.info("file ****taille:${file.size}")
        log.info("file ****name:${file.name}")
        val imageUri = gcsService.uploadFile(file,"realisation/$model/")
//        val filename = storageService.store(file, subfolder = "/realisation/$model/")
//        val fileUrl = "$server/realisation/$model/$filename"
//        log.info("public url local $fileUrl")
        p.path = imageUri!!
        val data = ServiceMaconRealisationEntity(
            service = p.service.toEntity(),
            name = "",
            path = p.path
        )
        return repository.save(data).toDomain()
    }
}