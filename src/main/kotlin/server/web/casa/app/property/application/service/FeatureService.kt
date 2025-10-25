package server.web.casa.app.property.application.service

import jakarta.persistence.EntityNotFoundException
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.Feature
import server.web.casa.app.property.infrastructure.persistence.mapper.FeatureMapper
import server.web.casa.app.property.infrastructure.persistence.repository.FeatureRepository

@Service
class FeatureService(
    private val repository: FeatureRepository,
    private val mapper : FeatureMapper
) {
    suspend fun create(p : Feature): Feature {
        val data = mapper.toEntity(p)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }
    suspend fun getAll() : List<Feature> = repository.findAll().map { mapper.toDomain(it) }.toList()

    suspend fun findByIdFeature(id : Long) : Feature? {
        val data = repository.findById(id)?.let {
            return mapper.toDomain(it)
        }?: EntityNotFoundException("Aucun equipement avec cet identifiant $id")
        return null
    }

}