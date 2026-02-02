package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.Vacance
import server.web.casa.app.property.domain.model.dto.VacanceDTO
import server.web.casa.app.property.domain.model.toEntity
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.repository.*

@Service
class VacanceService(
    private val repository: VacanceRepository,
    private val imageVacance : VacanceImageRepository,
) {
    suspend fun getAllVacance() = coroutineScope{
        repository.findAll().map { it.toDomain() }.toList()
    }

    suspend fun getAllVacanceByAgence(agence : Long) = coroutineScope{
       val data = repository.getAllByAgence(agence).toList()
       val vacance = mutableListOf<VacanceDTO>()
       val agenceIds: List<Long> = data.map { it?.id!! }
       val images = imageVacance.findByVacanceIdIn(agenceIds).toList()
       val imageByBureau = images.groupBy { it.vacanceId }
       data.forEach { ag->
           vacance.add(
              VacanceDTO(
                 vacance = ag!!.toDomain(),
                 image = imageByBureau[ag.id]?.map { it.toDomain() } ?: emptyList()
             )
          )
        }
        vacance
    }
    suspend fun create(data : Vacance) = coroutineScope {
       val result = repository.save(data.toEntity()).toDomain()
        result
    }
    suspend fun findById(id : Long) = coroutineScope {
        val result = repository.findById(id)?.toDomain()
        result
    }
    suspend fun getImageByVacanceID( vacanceId: Long) = coroutineScope {imageVacance.findByVacanceID (vacanceId).toList() }

    suspend fun update(p: Vacance) = coroutineScope {
        val data = p.toEntity()
        val result = repository.save(data)
        result.toDomain()
    }

}