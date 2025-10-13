package server.web.casa.app.property.application.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.Property
import server.web.casa.app.property.domain.model.PropertyType
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.PropertyMapper
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyRepository
import server.web.casa.app.user.domain.model.User
import kotlin.streams.toList

@Service
class PropertyService(
    private val repository: PropertyRepository,
    private val mapper : PropertyMapper
) {

    fun create(p : Property): PropertyEntity {
        val data = mapper.toEntity(p)
        val result = repository.save(data)
        return result
    }
    fun getAll() : List<Property> = repository.findAll().stream().map { mapper.toDomain(it) }.toList()

    fun findByIdProperty(id : Long) : Property? {
        val data = repository.findById(id).orElseThrow { ->
            EntityNotFoundException("Aucune proprièté avec cet identifiant $id")
        }
        return mapper.toDomain(data)
    }
}