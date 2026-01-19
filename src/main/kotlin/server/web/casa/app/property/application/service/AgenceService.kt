package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.property.domain.model.Agence
import server.web.casa.app.property.domain.model.dto.VacanceAgence
import server.web.casa.app.property.domain.model.toEntity
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.repository.AgenceRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService

@Service
class AgenceService(
    private val repository: AgenceRepository,
    private val gcsService: GcsService,
    private val vacance: VacanceService
    ) {
    suspend fun getAllAgence() = coroutineScope{
        val agenceByVacance = mutableListOf<VacanceAgence>()
        repository.findAll().collect {
           agenceByVacance.add(
               VacanceAgence(
                   agence = it.toDomain(),
                   site = vacance.getAllVacanceByAgence(it.id?:0)
               )
           )
        }
        agenceByVacance.toList()
    }

    suspend fun getAllByUser(userId : Long) = coroutineScope{
        val agenceByVacance = mutableListOf<VacanceAgence>()
        val data = repository.getAllByUser(userId)
        data.toList().forEach { agenceByVacance.add(VacanceAgence(agence = it?.toDomain(), site = vacance.getAllVacanceByAgence(it?.id ?: 0))) }
        agenceByVacance.toList()
    }

    suspend fun create(agence: Agence): Agence = coroutineScope {
        if (repository.countByUserId(agence.userId) != 0L) throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Vous ne pouvez pas créer plusieurs Agences")
        val file = base64ToMultipartFile(agence.logo?:"", "agence")
        val imageUri = gcsService.uploadFile(file,"agence/")
        agence.logo = imageUri
        repository.save(agence.toEntity()).toDomain()
    }
}