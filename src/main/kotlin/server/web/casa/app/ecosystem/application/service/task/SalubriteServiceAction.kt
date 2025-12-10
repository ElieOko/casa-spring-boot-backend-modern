package server.web.casa.app.ecosystem.application.service.task

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.ecosystem.domain.model.task.SalubriteTask
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.salubrite.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.repository.service.ServiceSalubriteRepository

@Service
class SalubriteServiceAction(
    private val repository: ServiceSalubriteRepository
) {
    fun create(data : SalubriteTask) = repository.save(data.toEntity()).toDomain()
    fun canCertified(id : Long): Pair<String, SalubriteTask> {
        val certification = repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST,"Ce service n'existe pas")
        }
        if (certification.isCertified){
            return Pair("Ce service est déjà certifié", certification.toDomain())
        }
        certification.isCertified = true
        return Pair("Service certifié avec succès",repository.save(certification).toDomain())
    }
    fun getAllData(): List<SalubriteTask> {
       val data = repository.findAll().filter { it.isActive && it.isCertified }
       return data.map{it.toDomain()}
    }
    fun canUpdate(id : Long, data : SalubriteTask): Pair<String, SalubriteTask> {
      val entity =  repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST,"Ce service n'existe pas")
        }.let {
        return@let data.toEntity()
        }
       return Pair("Modification effectué avec succès",repository.save(entity).toDomain())
    }
    fun getById(id : Long): SalubriteTask {
        val data = repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST,"Ce service n'existe pas")
        }
        return data.toDomain()
    }
    fun disableOrEnable(id : Long){

    }
}