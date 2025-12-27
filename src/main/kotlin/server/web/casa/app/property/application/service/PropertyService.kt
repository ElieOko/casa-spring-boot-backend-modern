package server.web.casa.app.property.application.service

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.slf4j.*
import org.springframework.http.*
import org.springframework.stereotype.*
import org.springframework.web.server.*
import server.web.casa.app.address.application.service.CityService
import server.web.casa.app.address.application.service.CommuneService
import server.web.casa.app.address.application.service.QuartierService
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.property.domain.model.*
import server.web.casa.app.property.domain.model.dto.*
import server.web.casa.app.property.domain.model.filter.*
import server.web.casa.app.property.infrastructure.persistence.entity.*
import server.web.casa.app.property.infrastructure.persistence.mapper.*
import server.web.casa.app.property.infrastructure.persistence.repository.*
import server.web.casa.app.user.application.service.*

@Service
class PropertyService(
    private val repository: PropertyRepository,
    private val propertyImageService: PropertyImageService,
    private val propertyImageLivingRoomService: PropertyImageLivingRoomService,
    private val propertyImageRoomService: PropertyImageRoomService,
    private val propertyImageKitchenService: PropertyImageKitchenService,
    private val userService: UserService,
    private val propertyTypeService: PropertyTypeService,
    private val featureService: FeatureService,
    private val repositoryFeature : PropertyFeatureRepository,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService,
    private val devise: DeviseService,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    suspend fun create(p: PropertyMasterDTO, features: List<FeatureRequest>): PropertyMasterDTO  = coroutineScope {
        val data = p.toEntity()
        val result = repository.save(data)
        features.forEach {
            repositoryFeature.save(PropertyFeatureEntity(
                propertyId = result.id!!,
                featureId = it.featureId
            ))
        }
        toDomain(result.id!!)
    }
    suspend fun update(p: PropertyMasterDTO) = coroutineScope {
        val data = p.toEntity()
        val result = repository.save(data)
        toDomain(result.id!!)
    }
    suspend fun getAll(page : Int, size : Int, sortBy : String, sortOrder : String): List<PropertyMasterDTO> {
//        val sort = if (sortOrder.equals("desc",true)) Sort.by(sortBy).descending()  else Sort.by(sortBy).ascending()
//        val pageable = PageRequest.of(page,repository.findAll().count(),sort)
//        val page = repository.findAll()
//        val data = page.map { it.toDomain() }
        return findAllRelation()
    }

    suspend fun findAllRelation(): List<PropertyMasterDTO> = coroutineScope {
        val properties = repository.findAll().toList()
        val propertyList = mutableListOf<PropertyMasterDTO>()
        val propertyIds: List<Long> = properties.map { it.id!! }
        //val propertyIds = properties.map { it.id } as List<Long>
        // Récupération groupée
        val features = repositoryFeature.findByPropertyIdIn(propertyIds).toList()
        val images = propertyImageService.findPropertyIdIn(propertyIds).toList()
        val roomImages = propertyImageRoomService.findPropertyIdIn(propertyIds).toList()
        val livingRoomImages = propertyImageLivingRoomService.findPropertyIdIn(propertyIds).toList()
        val kitchenImages = propertyImageKitchenService.findPropertyIdIn(propertyIds).toList()
        // Groupement par propertyId
        val featureByProperty = features.groupBy { it.propertyId }
        val imagesByProperty = images.groupBy { it.propertyId }
        val roomImagesByProperty = roomImages.groupBy { it.propertyId }
        val livingRoomImagesByProperty = livingRoomImages.groupBy { it.propertyId }
        val kitchenImagesByProperty = kitchenImages.groupBy { it.propertyId }
        // Assemblage final
        properties.forEach { property ->
            propertyList.add(PropertyMasterDTO(
                property = property.toPropertyDTO(),
                devise = devise.getById(property.deviseId),
                images = ImageDTO(
                    main = imagesByProperty[property.id] ?: emptyList(),
                    room = roomImagesByProperty[property.id] ?: emptyList(),
                    living = livingRoomImagesByProperty[property.id] ?: emptyList(),
                    kitchen = kitchenImagesByProperty[property.id] ?: emptyList()
                ).toImage(),
                address = property.toAddressDTO(),
                localAddress = LocalAddressDTO(
                    city = cityService.findByIdCity(property.cityId),
                    commune = communeService.findByIdCommune(property.communeId),
                    quartier = quartierService.findByIdQuartier(property.quartierId)
                ),
                geoZone = property.toGeo(),
                postBy = userService.findIdUser(property.user!!).username,
                typeProperty = propertyTypeService.findByIdPropertyType(property.propertyTypeId),
                features = featureByProperty[property.id]?.map { featureService.findByIdFeature(it.featureId) }?.toList()?:emptyList()
            ))
        }
        propertyList
    }

    private suspend fun toDomain(id : Long): PropertyMasterDTO = coroutineScope {
        val property = repository.findById(id)?: throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Cette proprièté n'existe pas")
        val propertyIds = listOf(id)
        // Récupération groupée
        val features = repositoryFeature.findByPropertyIdIn(propertyIds).toList()
        val images = propertyImageService.findPropertyIdIn(propertyIds).toList()
        val roomImages = propertyImageRoomService.findPropertyIdIn(propertyIds).toList()
        val livingRoomImages = propertyImageLivingRoomService.findPropertyIdIn(propertyIds).toList()
        val kitchenImages = propertyImageKitchenService.findPropertyIdIn(propertyIds).toList()
        // Groupement par propertyId
        val featureByProperty = features.groupBy { it.propertyId }
        val imagesByProperty = images.groupBy { it.propertyId }
        val roomImagesByProperty = roomImages.groupBy { it.propertyId }
        val livingRoomImagesByProperty = livingRoomImages.groupBy { it.propertyId }
        val kitchenImagesByProperty = kitchenImages.groupBy { it.propertyId }
        // Assemblage final
        PropertyMasterDTO(
            property = property.toPropertyDTO(),
            devise = devise.getById(property.deviseId),
            images = ImageDTO(
                main = imagesByProperty[id] ?: emptyList(),
                room = roomImagesByProperty[id] ?: emptyList(),
                living = livingRoomImagesByProperty[id] ?: emptyList(),
                kitchen = kitchenImagesByProperty[id] ?: emptyList()
            ).toImage(),
            address = property.toAddressDTO(),
            localAddress = LocalAddressDTO(
                city = cityService.findByIdCity(property.cityId),
                commune = communeService.findByIdCommune(property.communeId),
                quartier = quartierService.findByIdQuartier(property.quartierId)
            ),
            geoZone = property.toGeo(),
            postBy = userService.findIdUser(property.user!!).username,
            typeProperty = propertyTypeService.findByIdPropertyType(property.propertyTypeId),
            features = featureByProperty[property.id]?.map { featureService.findByIdFeature(it.featureId) }?.toList()?:emptyList()
        )
    }
    suspend fun getAllPropertyByUser(userId : Long): List<PropertyMasterDTO> {
        val data = findAllRelation().filter { it.property.userId == userId }.toList()
        return data
    }
    suspend fun findByIdProperty(id: Long): Pair<PropertyMasterDTO, Flow<Property>> {
        val data = toDomain(id)

        //|| (it.propertyType == data.propertyType)
        var similary = repository.findAll()
            .filter { it.cityValue == data.address.cityValue }
            .filter { it.communeValue == data.address.communeValue }
            .filter { it.transactionType == data.property.transactionType }
            .filter { it.id != data.property.propertyId }
        if (data.localAddress.city?.cityId == 1L){
            similary = repository.findAll()
                .filter { it.cityId == 1L }
                .filter {it.communeId == data.localAddress.commune?.communeId}
                .filter {it.transactionType == data.property.transactionType }
                .filter { it.id != data.property.propertyId }
        }
        val key = Pair(data,similary.map{it.toDomain()})
        return key
    }
    suspend fun filterProduct(
        filterModel : PropertyFilter,
        page : Int,
        size : Int,
        sortBy : String,
        sortOrder : String
    ): Flow<Property>{
//        val sort = if (sortOrder.equals("desc",true)) Sort.by(sortBy).descending()  else Sort.by(sortBy).ascending()
//        val pageable = PageRequest.of(page,size,sort)
        val data = repository.filterProperty(
            transactionType = filterModel.transactionType,
            minPrice = filterModel.minPrice,
            maxPrice = filterModel.maxPrice,
            city = filterModel.city,
            commune = filterModel.commune,
            typeMaison = filterModel.typeMaison,
            room = filterModel.room,
//            pageable = pageable
        )
        return data.map { it.toDomain() }
    }
}