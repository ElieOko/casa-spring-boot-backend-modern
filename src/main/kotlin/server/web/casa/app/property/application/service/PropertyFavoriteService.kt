package server.web.casa.app.property.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.PropertyFavorite
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyFavoriteEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.PropertyFavoriteMapper
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyFavoriteRepository

@Service
class PropertyFavoriteService(
    private val repository: PropertyFavoriteRepository,
    private val mapper : PropertyFavoriteMapper
) {

    suspend fun create(p : PropertyFavorite): PropertyFavoriteEntity {
        val data = mapper.toEntity(p)
        val result = repository.save(data)
        return result
    }
    suspend fun getAll() : List<PropertyFavorite> = repository.findAll().map { mapper.toDomain(it) }.toList()

    suspend fun remove(id : Long){
        repository.findById(id)?.let{
            repository.delete(it)
        }
    }
}