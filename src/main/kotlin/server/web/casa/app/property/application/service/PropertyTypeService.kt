package server.web.casa.app.property.application.service

import jakarta.persistence.EntityNotFoundException
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.PropertyType
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyTypeEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.PropertyTypeMapper
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyTypeRepository

@Service
class PropertyTypeService(
    private val repository: PropertyTypeRepository,
    private val mapper : PropertyTypeMapper
) {
    suspend fun create(p : PropertyType): PropertyType {
        val data = mapper.toEntity(p)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }
    suspend fun getAll() : List<PropertyType> = repository.findAll().map { mapper.toDomain(it) }.toList()

    suspend fun findByIdPropertyType(id : Long) : PropertyType? {
        val data = repository.findById(id).orElse(null)
        return mapper.toDomain(data)
    }
}