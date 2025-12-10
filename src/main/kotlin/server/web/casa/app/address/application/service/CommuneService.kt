package server.web.casa.app.address.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.address.domain.model.Commune
import server.web.casa.app.address.infrastructure.persistence.mapper.*
import server.web.casa.app.address.infrastructure.persistence.repository.CommuneRepository

@Service
class CommuneService(
    private val repository: CommuneRepository
) {
    suspend fun saveCommune(data: Commune): Commune? {
        val data = data.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun findAllCommune() = repository.findAll().map { it.toDomainOrigin() }.toList()

    suspend fun findByIdCommune(id : Long) : Commune? {
        val data = repository.findById(id).orElse(null)
        return data?.toDomain()
    }
}