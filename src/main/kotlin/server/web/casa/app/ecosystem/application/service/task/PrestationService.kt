package server.web.casa.app.ecosystem.application.service.task

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.ecosystem.domain.model.Prestation
import server.web.casa.app.ecosystem.domain.model.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.repository.PrestationRepository

@Service
class PrestationService(
    private val repository: PrestationRepository
) {
   suspend fun create(data : Prestation) = repository.save(data.toEntity()).toDomain()
   suspend fun canCertified(id : Long): Pair<String, Prestation> {
        val certification = repository.findById(id)?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce service n'existe pas")

        if (certification.isCertified){
            return Pair("Ce service est déjà certifié", certification.toDomain())
        }
        certification.isCertified = true
        return Pair("Service certifié avec succès",repository.save(certification).toDomain())
    }
   suspend fun getAllData(): Flow<Prestation> {
        val data = repository.findAll().filter { it.isActive && it.isCertified }
        return data.map{it.toDomain()}
    }
   suspend fun canUpdate(id : Long, data: Prestation): Pair<String, Prestation> {
        val entity =  repository.findById(id)?: throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Ce service n'existe pas")
        return Pair("Modification effectué avec succès",repository.save(entity).toDomain())
    }
   suspend fun getById(id : Long): Prestation {
        val data = repository.findById(id)?: throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Ce service n'existe pas")
        return data.toDomain()
    }
}