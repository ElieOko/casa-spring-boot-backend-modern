package server.web.casa.app.property.application.service

import jakarta.persistence.EntityNotFoundException
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
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

    suspend fun create(p : Property): Property {
        val data = mapper.toEntity(p)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }
    suspend fun getAll() : List<Property> = repository.findAll().stream().map {
        mapper.toDomain(it!!)
    }.toList()

    suspend fun findByIdProperty(id : Long) : Property? {
        val data = repository.findById(id).let {
            return mapper.toDomain(it.orElse(null))
        }
    }
}