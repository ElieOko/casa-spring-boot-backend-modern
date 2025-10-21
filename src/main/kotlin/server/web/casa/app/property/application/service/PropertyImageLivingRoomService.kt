package server.web.casa.app.property.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.PropertyImageLivingRoom
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageLivingRoomEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.PropertyImageLivingRoomMapper
import server.web.casa.app.property.infrastructure.persistence.mapper.PropertyMapper
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyImageLivingRoomRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService

@Service
class PropertyImageLivingRoomService(
    private val repository: PropertyImageLivingRoomRepository,
    private val mapper : PropertyMapper,
    private val gcsService: GcsService
) {
    fun create(p : PropertyImageLivingRoom): PropertyImageLivingRoomEntity {
        val file = base64ToMultipartFile(p.name,"living_room/")
        val imageUri = gcsService.uploadFile(file,"living/")
        p.path = imageUri!!
        p.name = file.originalFilename
        val data = PropertyImageLivingRoomEntity(
            propertyLiving = mapper.toEntity(p.property!!),
            name = p.name,
            path = p.path
        )
        val result = repository.save(data)
        return result
    }
//    fun getAll() : List<PropertyImageLivingRoom> = repository.findAll().stream().map { mapper.toDomain(it) }.toList()
}