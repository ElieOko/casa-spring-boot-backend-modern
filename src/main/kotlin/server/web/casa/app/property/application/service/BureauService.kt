package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.boot.context.properties.PropertyMapper
import org.springframework.stereotype.Service
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.app.payment.infrastructure.persistence.entity.DeviseEntity
import server.web.casa.app.property.domain.model.Bureau
import server.web.casa.app.property.domain.model.BureauDTOMaster
import server.web.casa.app.property.domain.model.FeatureRequest
import server.web.casa.app.property.domain.model.toEntity
import server.web.casa.app.property.infrastructure.persistence.entity.BureauFeatureEntity
import server.web.casa.app.property.infrastructure.persistence.entity.BureauImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyFeatureEntity
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.repository.BureauFeatureRepository
import server.web.casa.app.property.infrastructure.persistence.repository.BureauImageRepository
import server.web.casa.app.property.infrastructure.persistence.repository.BureauRepository
import kotlin.collections.map
import kotlin.collections.toList

@Service
class BureauService(
    private val repository: BureauRepository,
    private val bureauImageRepository: BureauImageRepository,
    private val devise: DeviseService,
    private val repositoryFeature: BureauFeatureRepository,
    private val featureService: FeatureService,
) {
    suspend fun getAllBureau() = coroutineScope{
       val data =  repository.findAll().toList()
       val bureauList = mutableListOf<BureauDTOMaster>()
       val bureauIds: List<Long> = data.map { it.id!! }
       val images = bureauImageRepository.findByBureauIdIn(bureauIds).toList()
       val features = repositoryFeature.findByBureauIdIn(bureauIds).toList()
       val imageByBureau: Map<Long, List<BureauImageEntity>> = images.groupBy { it.bureauId }
       val featureByModel = features.groupBy { it.bureauId }
       data.forEach { bureau->
            bureauList.add(
                BureauDTOMaster(
                    bureau = bureau.toDomain() ,
                    images = imageByBureau[bureau.id]?.map { it.toDomain() }?:emptyList(),
                    devise = devise.getById(bureau.deviseId!!),
                    feature = featureByModel[bureau.id]?.map { featureService.findByIdFeature(it.featureId) }?.toList()?:emptyList()
                )
            )
        }
       bureauList
    }

    suspend fun create(data : Bureau,features: List<FeatureRequest>) = coroutineScope {
       val result = repository.save(data.toEntity()).toDomain()
        features.forEach {
            repositoryFeature.save(
                BureauFeatureEntity(
                    bureauId = result.id!!,
                    featureId = it.featureId
                )
            )
        }
        result
    }
}