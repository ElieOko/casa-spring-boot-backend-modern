package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.Bureau
import server.web.casa.app.property.domain.model.toEntity
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.repository.BureauRepository

@Service
class BureauService(
    private val repository: BureauRepository,
) {
    suspend fun getAllBureau() = coroutineScope{
        repository.findAll().map { it.toDomain() }.toList()
    }

    suspend fun create(data : Bureau) = coroutineScope {
       val result = repository.save(data.toEntity()).toDomain()
        result
    }
}