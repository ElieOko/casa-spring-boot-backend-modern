package server.web.casa.app.property.application.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import server.web.casa.app.property.domain.model.PropertyImage
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.PropertyImageMapper
import server.web.casa.app.property.infrastructure.persistence.mapper.PropertyMapper
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyImageRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.convertFile
import server.web.casa.utils.gcs.GcsService

@Service
class PropertyImageService(
    private val repository: PropertyImageRepository,
    private val mapper : PropertyMapper,
//    private val mapper : PropertyMapper,
    private val gcsService: GcsService
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    fun create(p : PropertyImage): PropertyImageEntity {
        val file = base64ToMultipartFile(p.name, "property")
        log.info("file ****taille:${file.size}")
        log.info("file ****name:${file.name}")
        val imageUri = gcsService.uploadFile(file,"property/")
        log.info("file uri ****name:${imageUri}")
        p.path = imageUri!!
        p.name = file.originalFilename
        val data = PropertyImageEntity(
            property =mapper.toEntity(p.property!!),
            name = p.name,
            path = p.path
        )
        val result = repository.save(data)
        return result
    }
//    fun getAll() : List<PropertyImage> = repository.findAll().stream().map { mapper.toDomain(it) }.toList()
}