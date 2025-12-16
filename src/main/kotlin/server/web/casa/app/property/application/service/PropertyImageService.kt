package server.web.casa.app.property.application.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.PropertyImage
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.toEntity
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyImageRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.storage.FileSystemStorageService

@Service
class PropertyImageService(
    private val repository: PropertyImageRepository,
    private val gcsService: GcsService,
    private val storageService: FileSystemStorageService
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    suspend fun create(p : PropertyImage,server : String): PropertyImageEntity {
        val file = base64ToMultipartFile(p.name, "property")
        log.info("file ****taille:${file.size}")
        log.info("file ****name:${file.name}")
        val imageUri = gcsService.uploadFile(file,"property/images/")
        val filename = storageService.store(file, subfolder = "/property/images/")
        val fileUrl = "$server/property/images/$filename"
        log.info("public url local $fileUrl")
//        log.info("file uri ****name:${imageUri}")
        p.path = imageUri!!
//        p.name = file.name
        val data = PropertyImageEntity(
            propertyId = p.property?.propertyId!!,
            name = fileUrl,
            path = p.path
        )
        val result = repository.save(data)
        return result
    }
//    fun getAll() : List<PropertyImage> = repository.findAll().stream().map { mapper.toDomain(it) }.toList()
}