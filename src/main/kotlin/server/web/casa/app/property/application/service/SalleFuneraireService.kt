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
import server.web.casa.app.property.domain.model.SalleFuneraire
import server.web.casa.app.property.domain.model.SalleFuneraireDTOMaster
import server.web.casa.app.property.domain.model.toEntity
import server.web.casa.app.property.infrastructure.persistence.entity.BureauImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.repository.BureauImageRepository
import server.web.casa.app.property.infrastructure.persistence.repository.BureauRepository
import server.web.casa.app.property.infrastructure.persistence.repository.SalleFestiveImageRepository
import server.web.casa.app.property.infrastructure.persistence.repository.SalleFestiveRepository
import server.web.casa.app.property.infrastructure.persistence.repository.SalleFuneraireImageRepository
import server.web.casa.app.property.infrastructure.persistence.repository.SalleFuneraireRepository
import server.web.casa.app.user.application.service.UserService
import kotlin.collections.map

@Service
class SalleFuneraireService(
    private val repository: SalleFuneraireRepository,
    private val imageRepository: SalleFuneraireImageRepository,
    private val devise: DeviseService,
    private val userService: UserService,
) {
    suspend fun getAll() = coroutineScope{
       val data =  repository.findAll().toList()
        val dataList = mutableListOf<SalleFuneraireDTOMaster>()
       val bureauIds: List<Long> = data.map { it.id!! }
       val images = imageRepository.findBySalleFuneraireIdIn(bureauIds).toList()
       val imageByModel = images.groupBy { it.salleFuneraireId }
       data.forEach { m->
           dataList.add(
               SalleFuneraireDTOMaster(
                   funeraire = m.toDomain() ,
                   images = imageByModel[m.id]?.map { it.toDomain() }?:emptyList(),
                   devise = devise.getById(m.deviseId!!),
                   postBy = userService.findIdUser(m.userId!!).username
               ))
        }
        dataList
    }

    suspend fun create(data : SalleFuneraire) = coroutineScope {
       val result = repository.save(data.toEntity()).toDomain()
        result
    }
}