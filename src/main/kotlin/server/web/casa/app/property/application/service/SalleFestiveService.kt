package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.address.application.service.CityService
import server.web.casa.app.address.application.service.CommuneService
import server.web.casa.app.address.application.service.QuartierService
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.property.domain.model.FeatureRequest
import server.web.casa.app.property.domain.model.SalleFestive
import server.web.casa.app.property.domain.model.SalleFestiveDTOMaster
import server.web.casa.app.property.domain.model.dto.LocalAddressDTO
import server.web.casa.app.property.domain.model.toDTO
import server.web.casa.app.property.domain.model.toEntity
import server.web.casa.app.property.infrastructure.persistence.entity.FestiveFeatureEntity
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFestiveEntity
import server.web.casa.app.property.infrastructure.persistence.entity.toAddressDTO
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.entity.toGeo
import server.web.casa.app.property.infrastructure.persistence.repository.FestiveFeatureRepository
import server.web.casa.app.property.infrastructure.persistence.repository.SalleFestiveImageRepository
import server.web.casa.app.property.infrastructure.persistence.repository.SalleFestiveRepository
import server.web.casa.app.user.application.service.UserService
import kotlin.collections.map
import kotlin.collections.toList

@Service
class SalleFestiveService(
    private val repository: SalleFestiveRepository,
    private val imageRepository: SalleFestiveImageRepository,
    private val devise: DeviseService,
    private val userService: UserService,
    private val repositoryFeature: FestiveFeatureRepository,
    private val featureService: FeatureService,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService,
    private val propertyTypeService: PropertyTypeService,
) {
    private suspend fun findAll(data: List<SalleFestiveEntity>) = coroutineScope {
        val dataList = mutableListOf<SalleFestiveDTOMaster>()
        val ids : List<Long> = data.map { it.id!! }
        val images = imageRepository.findBySalleFestiveIdIn(ids).toList()
        val features = repositoryFeature.findByFestiveIdIn(ids).toList()
        val imageByModel = images.groupBy { it.salleFestiveId }
        val featureByModel = features.groupBy { it.festiveId }
        data.forEach { m->
            dataList.add(
                SalleFestiveDTOMaster(
                    festive = m.toDomain().toDTO() ,
                    images = imageByModel[m.id]?.map { it.toDomain() }?:emptyList(),
                    devise = devise.getById(m.deviseId!!),
                    address = m.toAddressDTO(),
                    localAddress = LocalAddressDTO(
                        city = cityService.findByIdCity(m.cityId),
                        commune = communeService.findByIdCommune(m.communeId),
                        quartier = quartierService.findByIdQuartier(m.quartierId)
                    ),
                    geoZone = m.toGeo(),
                    postBy = userService.findIdUser(m.userId!!).username,
                    feature = featureByModel[m.id]?.map { featureService.findByIdFeature(it.featureId) }?.toList()?:emptyList(),
                    typeProperty = propertyTypeService.findByIdPropertyType(m.propertyTypeId?:0),
                ))
        }
        dataList
    }
    suspend fun getAll() = coroutineScope{ findAll(repository.findAll().toList()) }

    suspend fun getAllPropertyByUser(userId : Long) = coroutineScope{findAll(repository.findAllByUser(userId).toList()) }

    suspend fun create(data : SalleFestive,features: List<FeatureRequest>) = coroutineScope {
       val result = repository.save(data.toEntity()).toDomain()
        features.forEach {
            repositoryFeature.save(FestiveFeatureEntity(
                festiveId = result.id!!,
                featureId = it.featureId
            ))
        }
        result
    }

    suspend fun findById(id : Long) = coroutineScope {
        repository.findById(id)?.toDomain()?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette proprièté n'existe pas de salle festive.")
    }
    suspend fun createOrUpdate(model : SalleFestive) =  coroutineScope{
        repository.save(model.toEntity())
    }

    suspend fun update(p: SalleFestive) = coroutineScope {
        val data = p.toEntity()
        val result = repository.save(data)
        result.toDomain()
    }
}