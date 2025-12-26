package server.web.casa.app.ecosystem.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.ecosystem.domain.model.Prestation
import server.web.casa.app.ecosystem.domain.model.PrestationDTOMaster
import server.web.casa.app.ecosystem.domain.model.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.repository.PrestationImageRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.repository.PrestationRepository
import server.web.casa.app.user.infrastructure.persistence.repository.AccountUserRepository
import kotlin.collections.map

@Service
class PrestationService(
    private val repository: PrestationRepository,
    private val account : AccountUserRepository,
    val repositoryImage: PrestationImageRepository
    ) {
   suspend fun create(data : Prestation) =  coroutineScope{
       if (repository.countByUserId(data.userId) == 2L) throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Vous ne pouvez pas créer plus de 2 prestations")
       account.findByUserAndAccount(data.userId, data.serviceId) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Ce service n'existe pas dans votre compte")
       val result = repository.findByUserAndService(data.userId,data.serviceId)
       if (result != null) throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Vous avez récemment créer une prestation") else repository.save(data.toEntity()).toDomain()
   }
   suspend fun canCertified(id : Long): Pair<String, Prestation> {
        val certification = repository.findById(id)?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce service n'existe pas")

        if (certification.isCertified){
            return Pair("Ce service est déjà certifié", certification.toDomain())
        }
        certification.isCertified = true
        return Pair("Service certifié avec succès",repository.save(certification).toDomain())
    }
   suspend fun getAllData()= coroutineScope {
       val data = repository.findAllFilter().toList()
       val prestationList = mutableListOf<PrestationDTOMaster>()
       val ids: List<Long> = data.map { it.id!! }
       val images = repositoryImage.findByPrestationIdIn(ids).toList()
       val imagesByPrestation = images.groupBy { it.prestationId }
       data.forEach { pres->
          prestationList.add(
              PrestationDTOMaster(
                  prestation = pres.toDomain(),
                  image = imagesByPrestation[pres.id]?.map { it.toDomain() } ?: emptyList()
              )
          )
       }
       prestationList
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