package server.web.casa.app.address.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.address.domain.model.Quartier
import server.web.casa.app.address.infrastructure.persistence.mapper.*
import server.web.casa.app.address.infrastructure.persistence.repository.QuartierRepository

@Service
class QuartierService(
    private val repository: QuartierRepository
) {
    suspend fun saveQuartier(data: Quartier): Quartier? {
        val data = data.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }

    suspend fun findAllQuartier() = repository.findAll().map { it.toDomain() }.toList()

    suspend fun findByIdQuartier(id : Long) : Quartier?{
        val data = repository.findById(id).orElse(null)
        return data?.toDomain()
    }
}