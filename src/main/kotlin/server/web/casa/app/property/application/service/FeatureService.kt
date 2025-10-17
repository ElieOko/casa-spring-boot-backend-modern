package server.web.casa.app.property.application.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.Feature
import server.web.casa.app.property.infrastructure.persistence.mapper.FeatureMapper
import server.web.casa.app.property.infrastructure.persistence.repository.FeatureRepository

@Service
class FeatureService(
    private val repository: FeatureRepository,
    private val mapper : FeatureMapper
) {
    fun create(p : Feature): Feature {
        val data = mapper.toEntity(p)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }
    fun getAll() : List<Feature> = repository.findAll().stream().map { mapper.toDomain(it) }.toList()

    fun findByIdPropertyType(id : Long) : Feature? {
        val data = repository.findById(id).orElseThrow { ->
            EntityNotFoundException("Aucun equipement avec cet identifiant $id")
        }
        return mapper.toDomain(data)
    }



}