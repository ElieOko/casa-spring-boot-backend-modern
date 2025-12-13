package server.web.casa.app.ecosystem.application.service.task

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.ecosystem.domain.model.task.MenusierTask
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.architect.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.menusier.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.salubrite.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.repository.service.ServiceMenusierRepository

@Service
class MenusierServiceAction(
    private val repository: ServiceMenusierRepository
) {
    fun create(data : MenusierTask) = repository.save(data.toEntity()).toDomain()
    fun canCertified(id : Long): Pair<String, MenusierTask> {
        val certification = repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST,"Ce service n'existe pas")
        }
        if (certification.isCertified){
            return Pair("Ce service est déjà certifié", certification.toDomain())
        }
        certification.isCertified = true
        return Pair("Service certifié avec succès",repository.save(certification).toDomain())
    }
    fun getAllData(): List<MenusierTask> {
        val data = repository.findAll().filter { it.isActive && it.isCertified }
        return data.map{it.toDomain()}
    }
    fun canUpdate(id : Long, data: MenusierTask): Pair<String, MenusierTask> {
        val entity =  repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST,"Ce service n'existe pas")
        }.let {
            return@let data.toEntity()
        }
        return Pair("Modification effectué avec succès",repository.save(entity).toDomain())
    }
    fun getById(id : Long): MenusierTask {
        val data = repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST,"Ce service n'existe pas")
        }
        return data.toDomain()
    }
    fun disableOrEnable(id : Long){

    }
}