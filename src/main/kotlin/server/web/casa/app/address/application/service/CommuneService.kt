package server.web.casa.app.address.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.address.domain.model.Commune
import server.web.casa.app.address.infrastructure.persistence.mapper.CommuneMapper
import server.web.casa.app.address.infrastructure.persistence.repository.CommuneRepository

@Service
class CommuneService(
    private val repository: CommuneRepository,
    private val mapper : CommuneMapper
) {
    suspend fun saveCommune(data: Commune): Commune {
        val data = mapper.toEntity(data)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }

    suspend fun findAllCommune() : List<Commune>{
        return repository.findAll().map { mapper.toDomainOrigin(it) }.toList()
    }

    suspend fun findByIdCommune(id : Long): Commune {
        repository.findById(id).let{ return mapper.toDomain(it.orElse(null)) }

    }
}