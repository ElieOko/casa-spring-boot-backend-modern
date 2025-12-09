package server.web.casa.app.property.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.PropertyFavorite
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyFavoriteEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.*
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyFavoriteRepository

@Service
class PropertyFavoriteService(
    private val repository: PropertyFavoriteRepository,
) {

    suspend fun create(p : PropertyFavorite): PropertyFavoriteEntity {
        val data = p.toEntity()
        val result = repository.save(data)
        return result
    }
    suspend fun getAll() : List<PropertyFavorite> = repository.findAll().map { it.toDomain() }.toList()

    suspend fun remove(id : Long){
        repository.findById(id).let{
            repository.delete(it.orElse(null))
        }
    }
}