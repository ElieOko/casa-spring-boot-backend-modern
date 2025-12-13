package server.web.casa.app.ecosystem.application.service.task

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.ecosystem.domain.model.task.AjusteurTask
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.salubrite.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.repository.service.ServiceAjusteurRepository

@Service
class AjusteurServiceAction(
    private val repository: ServiceAjusteurRepository
) {
    fun create(data : AjusteurTask) = repository.save(data.toEntity()).toDomain()

    fun canCertified(id : Long) : Pair<String, AjusteurTask> {
        val certification = repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST,"Ce service n'existe pas")
        }
        if (certification.isCertified){
           return Pair("Ce service est déjà certifié", certification.toDomain())
        }
        certification.isCertified = true
        return Pair("Service certifié avec succès",repository.save(certification).toDomain())
    }
    fun getAllData(): List<AjusteurTask> {
        val data = repository.findAll().filter { it.isActive && it.isCertified }
        return data.map{it.toDomain()}
    }
    fun canUpdate(id : Long, data: AjusteurTask): Pair<String, AjusteurTask> {
        val entity =  repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST,"Ce service n'existe pas")
        }.let {
            return@let data.toEntity()
        }
        return Pair("Modification effectué avec succès",repository.save(entity).toDomain())
    }
    fun getById(id : Long): AjusteurTask {
       val data = repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST,"Ce service n'existe pas")
        }
        return data.toDomain()
    }
    fun disableOrEnable(id : Long){

    }

}