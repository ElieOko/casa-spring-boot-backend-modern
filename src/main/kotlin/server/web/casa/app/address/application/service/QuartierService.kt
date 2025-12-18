package server.web.casa.app.address.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
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

    suspend fun findByIdQuartier(id: Long?) : Quartier?{
        val data = if (id == null) null else repository.findById(id)
        return data?.toDomain()
    }
}