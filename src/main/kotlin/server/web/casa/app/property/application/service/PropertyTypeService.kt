package server.web.casa.app.property.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.PropertyType
import server.web.casa.app.property.infrastructure.persistence.mapper.*
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyTypeRepository

@Service
class PropertyTypeService(
    private val repository: PropertyTypeRepository,
) {
    suspend fun create(p : PropertyType): PropertyType {
        val data = p.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun getAll() : List<PropertyType> = repository.findAll().map { it.toDomain() }.toList()

    suspend fun findByIdPropertyType(id : Long) : PropertyType? {
        val data = repository.findById(id).orElse(null)
        return data.toDomain()
    }
}