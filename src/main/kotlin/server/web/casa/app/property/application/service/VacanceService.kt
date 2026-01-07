package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.Terrain
import server.web.casa.app.property.domain.model.Vacance
import server.web.casa.app.property.domain.model.toEntity
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.repository.VacanceRepository

@Service
class VacanceService(
    private val repository: VacanceRepository,
) {
    suspend fun getAllVacance() = coroutineScope{
        repository.findAll().map { it.toDomain() }.toList()
    }

    suspend fun create(data : Vacance) = coroutineScope {
       val result = repository.save(data.toEntity()).toDomain()
        result
    }

    suspend fun update(p: Vacance) = coroutineScope {
        val data = p.toEntity()
        val result = repository.save(data)
        result.toDomain()
    }

}