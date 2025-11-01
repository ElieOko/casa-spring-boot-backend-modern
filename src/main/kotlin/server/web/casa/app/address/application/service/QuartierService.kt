package server.web.casa.app.address.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.address.domain.model.Commune
import server.web.casa.app.address.domain.model.Quartier
import server.web.casa.app.address.infrastructure.persistence.mapper.CommuneMapper
import server.web.casa.app.address.infrastructure.persistence.mapper.QuartierMapper
import server.web.casa.app.address.infrastructure.persistence.repository.CommuneRepository
import server.web.casa.app.address.infrastructure.persistence.repository.QuartierRepository

@Service
class QuartierService(
    private val repository: QuartierRepository,
    private val mapper : QuartierMapper
) {
    suspend fun saveQuartier(data: Quartier): Quartier {
        val data = mapper.toEntity(data)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }

    suspend fun findAllQuartier() : List<Quartier>{
        return repository.findAll().map { mapper.toDomain(it) }.toList()
    }

    suspend fun findByIdQuartier(id : Long): Quartier {
        repository.findById(id).let{ return mapper.toDomain(it.orElse(null)) }
    }
}