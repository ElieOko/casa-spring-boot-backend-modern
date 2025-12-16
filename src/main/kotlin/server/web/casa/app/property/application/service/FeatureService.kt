package server.web.casa.app.property.application.service

import kotlinx.coroutines.flow.map
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.property.domain.model.Feature
import server.web.casa.app.property.infrastructure.persistence.mapper.*
import server.web.casa.app.property.infrastructure.persistence.repository.FeatureRepository

@Service
class FeatureService(
    private val repository: FeatureRepository
) {
    suspend fun create(p : Feature): Feature? {
        val data = p.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun getAll() = repository.findAll().map { it.toDomain() }

    suspend fun findByIdFeature(id : Long) : Feature {
       val data = repository.findById(id)?: throw ResponseStatusException(
           HttpStatusCode.valueOf(404),
           "ID Is Not Found."
       )
        return data.toDomain()
    }
}