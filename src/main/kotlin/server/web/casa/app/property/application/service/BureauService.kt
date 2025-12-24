package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.app.payment.infrastructure.persistence.entity.DeviseEntity
import server.web.casa.app.property.domain.model.Bureau
import server.web.casa.app.property.domain.model.BureauDTOMaster
import server.web.casa.app.property.domain.model.toEntity
import server.web.casa.app.property.infrastructure.persistence.entity.BureauImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.repository.BureauImageRepository
import server.web.casa.app.property.infrastructure.persistence.repository.BureauRepository
import kotlin.collections.map

@Service
class BureauService(
    private val repository: BureauRepository,
    private val bureauImageRepository: BureauImageRepository,
    private val devise: DeviseService
) {
    suspend fun getAllBureau() = coroutineScope{
       val data =  repository.findAll().toList()
        val bureauList = mutableListOf<BureauDTOMaster>()
       val bureauIds: List<Long> = data.map { it.id!! }
       val images = bureauImageRepository.findByBureauIdIn(bureauIds).toList()
       val imageByBureau: Map<Long, List<BureauImageEntity>> = images.groupBy { it.bureauId }

       data.forEach { bureau->
            bureauList.add(BureauDTOMaster(bureau = bureau.toDomain() , images = imageByBureau[bureau.id]?.map { it.toDomain() }?:emptyList(), devise = devise.getById(bureau.deviseId!!)))
        }
       bureauList
    }

    suspend fun create(data : Bureau) = coroutineScope {
       val result = repository.save(data.toEntity()).toDomain()
        result
    }
}