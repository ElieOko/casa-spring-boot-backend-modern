package server.web.casa.app.payment.application.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.ecosystem.domain.model.task.AjusteurTask
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.ajusteur.toDomain
import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.app.payment.domain.model.toEntity
import server.web.casa.app.payment.infrastructure.persistence.entity.toDomain
import server.web.casa.app.payment.infrastructure.persistence.repository.DeviseRepository

@Service
class DeviseService(
    private val repository : DeviseRepository
) {
    fun create(data : Devise) = repository.save(data.toEntity()).toDomain()

    fun getAllData(): List<Devise> {
        val data = repository.findAll()
        return data.map{it.toDomain()}
    }

    fun getById(id : Long): Devise {
        val data = repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST,"Cette devise n'existe pas")
        }
        return data.toDomain()
    }

}