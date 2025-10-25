package server.web.casa.app.property.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.PropertyImageKitchen
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageKitchenEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.PropertyImageKitchenMapper
import server.web.casa.app.property.infrastructure.persistence.mapper.PropertyMapper
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyImageKitchenRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService

@Service
class PropertyImageKitchenService(
    private val repository: PropertyImageKitchenRepository,
    private val mapper : PropertyMapper,
    private val gcsService: GcsService
) {
    suspend fun create(p : PropertyImageKitchen): PropertyImageKitchenEntity {
        val file = base64ToMultipartFile(p.name,"kitchen")
        val imageUri = gcsService.uploadFile(file,"kitchen/")
        p.path = imageUri!!
        p.name = file.originalFilename
        val data = PropertyImageKitchenEntity(
            propertyKitchen =mapper.toEntity(p.property!!),
            name = p.name,
            path = p.path
        )
        val result = repository.save(data)
        return result
    }
//    fun getAll() : List<PropertyImageKitchen> = repository.findAll().stream().map { mapper.toDomain(it) }.toList()
}