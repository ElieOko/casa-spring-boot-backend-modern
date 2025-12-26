package server.web.casa.app.property.application.service

import kotlinx.coroutines.flow.map
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
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
    suspend fun getAll() = repository.findAll().map { it.toDomain() }

    suspend fun findByIdPropertyType(id : Long) : PropertyType {
        val data = repository.findById(id)?: throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "ID Is Not Found. Property type"
        )
        return data.toDomain()
    }
}