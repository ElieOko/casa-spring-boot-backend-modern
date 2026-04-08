package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.actor.infrastructure.persistence.repository.PersonRepository
import server.web.casa.app.address.application.service.*
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.property.domain.model.*
import server.web.casa.app.property.domain.model.dto.LocalAddressDTO
import server.web.casa.app.property.domain.model.filter.PropertyFilter
import server.web.casa.app.property.infrastructure.persistence.entity.*
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.mapper.toDomain
import server.web.casa.app.property.infrastructure.persistence.repository.*
import server.web.casa.app.user.application.service.UserService
import kotlin.collections.*
import kotlin.collections.get

@Service
class BureauService(
    private val repository: BureauRepository,
    private val bureauImageRepository: BureauImageRepository,
    private val devise: DeviseService,
    private val person : PersonRepository,
    private val repositoryFeature: BureauFeatureRepository,
    private val featureService: FeatureService,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService,
    private val userService: UserService,
    private val propertyTypeService: PropertyTypeService,
) {
    private suspend fun findAll(data: List<BureauEntity>)  = coroutineScope {
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
                    image = person.findByUser(bureau.userId!!)?.images?:"",
                    localAddress = LocalAddressDTO(
                        city = cityService.findByIdCity(bureau.cityId),
                        commune = communeService.findByIdCommune(bureau.communeId),
                        quartier = quartierService.findByIdQuartier(bureau.quartierId)
                    ),
                    geoZone = bureau.toGeo(),
                    postBy = userService.findIdUser(bureau.userId).username,
                    typeProperty = propertyTypeService.findByIdPropertyType(bureau.propertyTypeId?:0),
                )
            )
        }
        bureauList
    }
    suspend fun getAllBureau(state: Boolean = false) = coroutineScope {
        val data = if (!state) repository.findAll().toList() else repository.findAllData().toList()
        findAll(data)
    }
    suspend fun getImageByBureauID( bureauId: Long)= coroutineScope { bureauImageRepository.findAllByBureauId(bureauId).toList() }
    suspend fun getAllPropertyByUser(userId : Long) = coroutineScope{ findAll(repository.findAllByUser(userId).toList()) }

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
    suspend fun findByNoRestrict(id : Long) = coroutineScope {
        repository.findByIdNoRestrict(id)?.toDomain()?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette proprièté n'existe.")
    }

    suspend fun update(p: Bureau) = coroutineScope {
        val data = p.toEntity()
        val result = repository.save(data)
        result.toDomain()
    }
    suspend fun createOrUpdate(model : Bureau) =  coroutineScope{
        repository.save(model.toEntity())
    }

    suspend fun filter(filterModel: PropertyFilter, page: Int, size: Int, sortBy: String, sortOrder: String) = coroutineScope {
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
                    image = person.findByUser(bureau.userId!!)?.images?:"",
                    feature = featureByModel[bureau.id]?.map { featureService.findByIdFeature(it.featureId) }?.toList() ?: emptyList(),
                    address = bureau.toAddressDTO(),
                    localAddress = LocalAddressDTO(city = cityService.findByIdCity(bureau.cityId), commune = communeService.findByIdCommune(bureau.communeId), quartier = quartierService.findByIdQuartier(bureau.quartierId)),
                    geoZone = bureau.toGeo(),
                    postBy = userService.findIdUser(bureau.userId).username,
                    typeProperty = propertyTypeService.findByIdPropertyType(bureau.propertyTypeId?:0),
                )
            )
        }
        bureauList
    }

    suspend fun showDetail(id : Long, state: Boolean = true) = coroutineScope{
        val data = (if (state) repository.findById(id) else repository.findByIdNoRestrict(id))?:throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette chambre n'existe.")
        val bureauList = mutableListOf<BureauDTOMaster>()
        val bureauIds: List<Long> = listOf(data.id!!)
        val images = bureauImageRepository.findByBureauIdIn(bureauIds).toList()
        val features = repositoryFeature.findByBureauIdIn(bureauIds).toList()
        val imageByBureau: Map<Long, List<BureauImageEntity>> = images.groupBy { it.bureauId }
        val featureByModel = features.groupBy { it.bureauId }
        bureauList.add(
            BureauDTOMaster(
                bureau = data.toDomain().toDT0(),
                images = imageByBureau[data.id]?.map { it.toDomain() } ?: emptyList(),
                devise = devise.getById(data.deviseId!!),
                image = person.findByUser(data.userId!!)?.images?:"",
                feature = featureByModel[data.id]?.map { featureService.findByIdFeature(it.featureId) }?.toList() ?: emptyList(),
                address = data.toAddressDTO(),
                localAddress = LocalAddressDTO(
                    city = cityService.findByIdCity(data.cityId),
                    commune = communeService.findByIdCommune(data.communeId),
                    quartier = quartierService.findByIdQuartier(data.quartierId)),
                geoZone = data.toGeo(),
                postBy = userService.findIdUser(data.userId).username,
                typeProperty = propertyTypeService.findByIdPropertyType(data.propertyTypeId?:0)
            )
        )

        bureauList
    }
}