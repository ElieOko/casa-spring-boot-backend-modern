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
import server.web.casa.app.property.domain.model.SalleFestive
import server.web.casa.app.property.domain.model.SalleFestiveDTOMaster
import server.web.casa.app.property.domain.model.toEntity
import server.web.casa.app.property.infrastructure.persistence.entity.BureauImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.repository.BureauImageRepository
import server.web.casa.app.property.infrastructure.persistence.repository.BureauRepository
import server.web.casa.app.property.infrastructure.persistence.repository.SalleFestiveImageRepository
import server.web.casa.app.property.infrastructure.persistence.repository.SalleFestiveRepository
import server.web.casa.app.user.application.service.UserService
import kotlin.collections.map

@Service
class SalleFestiveService(
    private val repository: SalleFestiveRepository,
    private val imageRepository: SalleFestiveImageRepository,
    private val devise: DeviseService,
    private val userService: UserService,
) {
    suspend fun getAll() = coroutineScope{
       val data =  repository.findAll().toList()
        val dataList = mutableListOf<SalleFestiveDTOMaster>()
       val bureauIds: List<Long> = data.map { it.id!! }
       val images = imageRepository.findBySalleFestiveIdIn(bureauIds).toList()
       val imageByModel = images.groupBy { it.salleFestiveId }

       data.forEach { m->
           dataList.add(
               SalleFestiveDTOMaster(
                   festive = m.toDomain() ,
                   images = imageByModel[m.id]?.map { it.toDomain() }?:emptyList(),
                   devise = devise.getById(m.deviseId!!),
                   postBy = userService.findIdUser(m.userId!!).username
               ))
        }
        dataList
    }

    suspend fun create(data : SalleFestive) = coroutineScope {
       val result = repository.save(data.toEntity()).toDomain()
        result
    }
}