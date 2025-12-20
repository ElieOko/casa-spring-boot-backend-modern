package server.web.casa.app.payment.application.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.app.payment.domain.model.toEntity
import server.web.casa.app.payment.infrastructure.persistence.entity.toDomain
import server.web.casa.app.payment.infrastructure.persistence.repository.DeviseRepository

@Service
class DeviseService(
    private val repository : DeviseRepository
) {
    suspend fun create(data : Devise) = repository.save(data.toEntity()).toDomain()

    fun getAllData(): Flow<Devise> {
        val data = repository.findAll()
        return data.map{it.toDomain()}
    }

    suspend fun getById(id : Long?): Devise? {
        if (id != null) {
            val data = repository.findById(id) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Cette devise n'existe pas")
            return data.toDomain()
        }
        return null
    }
}