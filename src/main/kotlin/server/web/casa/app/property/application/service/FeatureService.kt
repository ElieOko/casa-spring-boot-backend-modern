package server.web.casa.app.property.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.Feature
import server.web.casa.app.property.infrastructure.persistence.mapper.*
import server.web.casa.app.property.infrastructure.persistence.repository.FeatureRepository

@Service
class FeatureService(
    private val repository: FeatureRepository
) {
    suspend fun create(p : Feature): Feature {
        val data = p.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun getAll() : List<Feature> = repository.findAll().map { it.toDomain() }.toList()
    suspend fun findByIdFeature(id : Long) : Feature? {
         repository.findById(id).let {
           val d = it.orElse(null)
            return d.toDomain()
        }
    }
}