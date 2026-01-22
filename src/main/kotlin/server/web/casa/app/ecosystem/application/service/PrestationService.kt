package server.web.casa.app.ecosystem.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.actor.domain.model.UserPerson
import server.web.casa.app.ecosystem.domain.model.*
import server.web.casa.app.ecosystem.domain.model.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.*
import server.web.casa.app.ecosystem.infrastructure.persistence.repository.*
import server.web.casa.app.user.application.service.UserService
import server.web.casa.app.user.infrastructure.persistence.repository.AccountUserRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import kotlin.collections.*

@Service
class PrestationService(
    private val repository: PrestationRepository,
    private val account : AccountUserRepository,
    private val userService : UserService,
    private val gcsService: GcsService,
    val repositoryImage: PrestationImageRepository
    ) {
    private val model = "profile"
   suspend fun create(data : Prestation) =  coroutineScope{
       val file = base64ToMultipartFile(data.profile?:"", "${model}_prestation")
       if (repository.countByUserId(data.userId) == 2L) throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Vous ne pouvez pas créer plus de 2 prestations")
       account.findByUserAndAccount(data.userId, data.serviceId) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Ce service n'existe pas dans votre compte")
       val result = repository.findByUserAndService(data.userId,data.serviceId)
       val imageUri = gcsService.uploadFile(file,"prestation/$model/")
       data.profile = imageUri!!
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
      compactModel(repository.findAllFilter().toList())
   }

   private suspend fun compactModel(model : List<PrestationEntity>) = coroutineScope {
        val prestationList = mutableListOf<PrestationDTOMaster>()
        val ids =  model.map { it.id!! }
        val images = repositoryImage.findByPrestationIdIn(ids).toList()
        val imagesByPrestation = images.groupBy { it.prestationId }
        model.forEach { pres->
            prestationList.add(
                PrestationDTOMaster(
                    prestation = pres.toDomain(),
                    image = imagesByPrestation[pres.id]?.map { it.toDomain() } ?: emptyList(),
                    postBy = userService.findIdUser(pres.userId).username,
                )
            )
        }
        prestationList
    }
   private suspend fun compactModelAdmin(model : List<PrestationEntity>) = coroutineScope {
        val prestationList = mutableListOf<PrestationDTOMasterAdmin>()
        val ids =  model.map { it.id!! }
        val images = repositoryImage.findByPrestationIdIn(ids).toList()
        val imagesByPrestation = images.groupBy { it.prestationId }
        model.forEach { pres->
            val user = userService.findIdUser(pres.userId)
            prestationList.add(
                PrestationDTOMasterAdmin(
                    prestation = pres.toDomain(),
                    image = imagesByPrestation[pres.id]?.map { it.toDomain() } ?: emptyList(),
                    postBy = user.username,
                    actor = UserPerson(user = user,member = userService.findPersonByUser(user.userId?:0) )
                )
            )
        }
        prestationList
    }
   suspend fun getAllPrestationByUser(userId : Long) = coroutineScope {
        val data = repository.findAllFindByUser(userId).toList()
        compactModel(data)
    }
   suspend fun canUpdate(id : Long, data: Prestation): Pair<String, Prestation> {
      val entity =  repository.findById(id)?: throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Ce service n'existe pas")
      return Pair("Modification effectué avec succès",repository.save(entity).toDomain())
   }
   suspend fun getByIdPrestation(id : Long) = coroutineScope {
        val data = repository.findByIdPrestation(id).toList()
        if (data.isEmpty()) throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Ce service n'existe pas")
        compactModel(data)
    }

    suspend fun getById(id: Long)  = repository.findById(id)
}