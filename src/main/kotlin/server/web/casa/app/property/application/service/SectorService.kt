package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.repository.SectorRepository

@Service
class SectorService(
    private val repository: SectorRepository,
) {
    suspend fun getAllSector() = coroutineScope{
        repository.findAll().map { it.toDomain() }.toList()
    }
}