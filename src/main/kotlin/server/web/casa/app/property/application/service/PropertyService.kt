package server.web.casa.app.property.application.service

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.slf4j.*
import org.springframework.data.domain.*
import org.springframework.http.*
import org.springframework.stereotype.*
import org.springframework.web.server.*
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
    private val repositoryFeature : PropertyFeatureRepository
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    suspend fun create(p: Property, features: List<FeatureRequest>): PropertyMasterDTO {
        val data = p.toEntity()
        val result = repository.save(data)
        features.forEach {
            repositoryFeature.save(PropertyFeatureEntity(
                propertyId = result.id,
                featureId = it.featureId
            ))
        }

        return toDomain(result.id)
    }
    suspend fun getAll(
        page : Int,
        size : Int,
        sortBy : String,
        sortOrder : String
    ) : Page<Property> {
        val sort = if (sortOrder.equals("desc",true)) Sort.by(sortBy).descending()  else Sort.by(sortBy).ascending()
        val pageable = PageRequest.of(page,repository.findAll().count(),sort)
        val page = repository.findAll(pageable)
        val data = page.map { it.toDomain() }
        return data
    }
     suspend fun findAllRelation(): List<PropertyMasterDTO> = coroutineScope {
        val properties = repository.findAll().toList()
        val propertyIds = properties.map { it.id }
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
        properties.map { property ->
            PropertyMasterDTO(
                property = property.toPropertyDTO(),
                images = ImageDTO(
                    main = imagesByProperty[property.id] ?: emptyList(),
                    room = roomImagesByProperty[property.id] ?: emptyList(),
                    living = livingRoomImagesByProperty[property.id] ?: emptyList(),
                    kitchen = kitchenImagesByProperty[property.id] ?: emptyList()
                ).toImage(),
                address = property.toAddressDTO(),
                geoZone = property.toGeo(),
                postBy = userService.findIdUser(property.user!!).username,
                typeProperty = propertyTypeService.findByIdPropertyType(property.propertyTypeId),
                features = featureByProperty[property.id]?.map { featureService.findByIdFeature(it.featureId) }?.toList()?:emptyList()
            )
        }
    }

    private suspend fun toDomain(id : Long): PropertyMasterDTO = coroutineScope {
        val property = repository.findById(id)
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
            property = property!!.toPropertyDTO(),
            images = ImageDTO(
                main = imagesByProperty[id] ?: emptyList(),
                room = roomImagesByProperty[id] ?: emptyList(),
                living = livingRoomImagesByProperty[id] ?: emptyList(),
                kitchen = kitchenImagesByProperty[id] ?: emptyList()
            ).toImage(),
            address = property.toAddressDTO(),
            geoZone = property.toGeo(),
            postBy = userService.findIdUser(property.user!!).username,
            typeProperty = propertyTypeService.findByIdPropertyType(property.propertyTypeId),
            features = featureByProperty[property.id]?.map { featureService.findByIdFeature(it.featureId) }?.toList()?:emptyList()
        )
    }
    suspend fun getAllPropertyByUser(userId : Long): Flow<Property> {
        val data = repository.findAll().filter { it.user == userId }
        return data.map { it.toDomain() }
    }
    suspend fun findByIdProperty(id: Long): Pair<Property, Flow<Property>> {
        val property = repository.findById(id)?: throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Cette proprièté n'existe pas")
        val data = property.toDomain()
        //|| (it.propertyType == data.propertyType)
        var similary = repository.findAll()
            .filter { it.cityValue == data.cityValue }
            .filter { it.communeValue == data.communeValue }
            .filter { it.transactionType == data.transactionType }
            .filter { it.id != data.propertyId }
        if (data.city == 1L){
            similary = repository.findAll()
                .filter { it.cityId == 1L }
                .filter {it.communeId == data.commune}
                .filter {it.transactionType == data.transactionType }
                .filter { it.id != data.propertyId }
//
//                .filter { it.commune?.communeId == data.commune?.communeId}
//                .filter { it.propertyId != data.propertyId }
//                .filter {
//                    it.transactionType == data.transactionType
//                }
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
    ): Page<Property>{
        val sort = if (sortOrder.equals("desc",true)) Sort.by(sortBy).descending()  else Sort.by(sortBy).ascending()
        val pageable = PageRequest.of(page,size,sort)
        val data = repository.filterProperty(
            transactionType = filterModel.transactionType,
            minPrice = filterModel.minPrice,
            maxPrice = filterModel.maxPrice,
            city = filterModel.city,
            commune = filterModel.commune,
            typeMaison = filterModel.typeMaison,
            room = filterModel.room,
            pageable = pageable
        )
        return data.map { it.toDomain() }
    }
}