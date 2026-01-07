package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.address.application.service.*
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.property.domain.model.*
import server.web.casa.app.property.domain.model.dto.LocalAddressDTO
import server.web.casa.app.property.domain.model.filter.PropertyFilter
import server.web.casa.app.property.infrastructure.persistence.entity.*
import server.web.casa.app.property.infrastructure.persistence.mapper.toDomain
import server.web.casa.app.property.infrastructure.persistence.repository.*
import server.web.casa.app.user.application.service.UserService
import kotlin.collections.map
import kotlin.collections.toList

@Service
class BureauService(
    private val repository: BureauRepository,
    private val bureauImageRepository: BureauImageRepository,
    private val devise: DeviseService,
    private val repositoryFeature: BureauFeatureRepository,
    private val featureService: FeatureService,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService,
    private val userService: UserService,
    private val propertyTypeService: PropertyTypeService,
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
                    bureau = bureau.toDomain().toDT0(),
                    images = imageByBureau[bureau.id]?.map { it.toDomain() } ?: emptyList(),
                    devise = devise.getById(bureau.deviseId!!),
                    feature = featureByModel[bureau.id]?.map { featureService.findByIdFeature(it.featureId) }?.toList() ?: emptyList(),
                    address = bureau.toAddressDTO(),
                    localAddress = LocalAddressDTO(
                        city = cityService.findByIdCity(bureau.cityId),
                        commune = communeService.findByIdCommune(bureau.communeId),
                        quartier = quartierService.findByIdQuartier(bureau.quartierId)
                    ),
                    geoZone = bureau.toGeo(),
                    postBy = userService.findIdUser(bureau.userId!!).username,
                    typeProperty = propertyTypeService.findByIdPropertyType(bureau.propertyTypeId?:0),
                )
            )
        }
       bureauList
    }
    suspend fun getAllPropertyByUser(userId : Long) = coroutineScope{
       val data = getAllBureau().filter { it.bureau.userId == userId }.toList()
       data
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
    suspend fun findById(id : Long) = coroutineScope {
        repository.findById(id)?.toDomain()?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette proprièté n'existe pas de Bureau.")
    }

    suspend fun update(p: Bureau) = coroutineScope {
        val data = p.toEntity()
        val result = repository.save(data)
        result.toDomain()
    }

    suspend fun filter(
        filterModel : PropertyFilter,
        page : Int,
        size : Int,
        sortBy : String,
        sortOrder : String
    ) =  coroutineScope {
        val data = repository.filter(
            transactionType = filterModel.transactionType,
            minPrice = filterModel.minPrice,
            maxPrice = filterModel.maxPrice,
            city = filterModel.city,
            commune = filterModel.commune,
            typeMaison = filterModel.typeMaison,
            cityValue = filterModel.cityValue,
            communeValue = filterModel.communeValue,
        ).toList()
        val bureauList = mutableListOf<BureauDTOMaster>()
        val bureauIds: List<Long> = data.map { it.id!! }
        val images = bureauImageRepository.findByBureauIdIn(bureauIds).toList()
        val features = repositoryFeature.findByBureauIdIn(bureauIds).toList()
        val imageByBureau: Map<Long, List<BureauImageEntity>> = images.groupBy { it.bureauId }
        val featureByModel = features.groupBy { it.bureauId }
        data.forEach { bureau->
            bureauList.add(
                BureauDTOMaster(
                    bureau = bureau.toDomain().toDT0(),
                    images = imageByBureau[bureau.id]?.map { it.toDomain() } ?: emptyList(),
                    devise = devise.getById(bureau.deviseId!!),
                    feature = featureByModel[bureau.id]?.map { featureService.findByIdFeature(it.featureId) }?.toList() ?: emptyList(),
                    address = bureau.toAddressDTO(),
                    localAddress = LocalAddressDTO(city = cityService.findByIdCity(bureau.cityId), commune = communeService.findByIdCommune(bureau.communeId), quartier = quartierService.findByIdQuartier(bureau.quartierId)),
                    geoZone = bureau.toGeo(),
                    postBy = userService.findIdUser(bureau.userId!!).username,
                    typeProperty = propertyTypeService.findByIdPropertyType(bureau.propertyTypeId?:0),
                )
            )
        }
        bureauList
    }
}