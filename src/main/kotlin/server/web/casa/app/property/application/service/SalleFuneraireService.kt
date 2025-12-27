package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.address.application.service.*
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.property.domain.model.FeatureRequest
import server.web.casa.app.property.domain.model.SalleFestive
import server.web.casa.app.property.domain.model.SalleFuneraire
import server.web.casa.app.property.domain.model.SalleFuneraireDTOMaster
import server.web.casa.app.property.domain.model.dto.LocalAddressDTO
import server.web.casa.app.property.domain.model.toEntity
import server.web.casa.app.property.infrastructure.persistence.entity.FuneraireFeatureEntity
import server.web.casa.app.property.infrastructure.persistence.entity.toAddressDTO
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.entity.toGeo
import server.web.casa.app.property.infrastructure.persistence.repository.*
import server.web.casa.app.user.application.service.UserService
import kotlin.collections.map

@Service
class SalleFuneraireService(
    private val repository: SalleFuneraireRepository,
    private val imageRepository: SalleFuneraireImageRepository,
    private val devise: DeviseService,
    private val userService: UserService,
    private val repositoryFeature: FuneraireFeatureRepository,
    private val featureService: FeatureService,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService,
    private val propertyTypeService: PropertyTypeService,
) {
    suspend fun getAll() = coroutineScope{
       val data =  repository.findAll().toList()
       val dataList = mutableListOf<SalleFuneraireDTOMaster>()
       val ids: List<Long> = data.map { it.id!! }
       val images = imageRepository.findBySalleFuneraireIdIn(ids).toList()
       val features = repositoryFeature.findByFuneraireIdIn(ids).toList()
       val imageByModel = images.groupBy { it.salleFuneraireId }
       val featureByModel = features.groupBy { it.funeraireId }
       data.forEach { m->
           dataList.add(
               SalleFuneraireDTOMaster(
                   funeraire = m.toDomain() ,
                   images = imageByModel[m.id]?.map { it.toDomain() }?:emptyList(),
                   devise = devise.getById(m.deviseId?:0),
                   feature = featureByModel[m.id]?.map { featureService.findByIdFeature(it.featureId) }?.toList()?:emptyList(),
                   address = m.toAddressDTO(),
                   localAddress = LocalAddressDTO(
                       city = cityService.findByIdCity(m.cityId),
                       commune = communeService.findByIdCommune(m.communeId),
                       quartier = quartierService.findByIdQuartier(m.quartierId)
                   ),
                   geoZone = m.toGeo(),
                   postBy = userService.findIdUser(m.userId!!).username,
                   typeProperty = propertyTypeService.findByIdPropertyType(m.propertyTypeId?:0),
               ))
        }
        dataList
    }
    suspend fun getAllPropertyByUser(userId : Long) = coroutineScope{
        val data = getAll().filter { it.funeraire.userId == userId }.toList()
        data
    }
    suspend fun create(data : SalleFuneraire,features: List<FeatureRequest>) = coroutineScope {
       val result = repository.save(data.toEntity()).toDomain()
        features.forEach {
            repositoryFeature.save(FuneraireFeatureEntity(
                funeraireId = result.id!!,
                featureId = it.featureId
            ))
        }
        result
    }

    suspend fun findById(id : Long) = coroutineScope {
        repository.findById(id)?.toDomain()?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette proprièté n'existe pas de salle funeraire.")
    }

    suspend fun update(p: SalleFuneraire) = coroutineScope {
        val data = p.toEntity()
        val result = repository.save(data)
        result.toDomain()
    }
}