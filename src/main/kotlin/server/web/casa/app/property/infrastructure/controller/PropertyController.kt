package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.address.application.service.*
import server.web.casa.app.property.application.service.*
import server.web.casa.app.property.domain.model.Property
import server.web.casa.app.property.domain.model.PropertyFeature
import server.web.casa.app.property.domain.model.PropertyImage
import server.web.casa.app.property.domain.model.PropertyImageKitchen
import server.web.casa.app.property.domain.model.PropertyImageLivingRoom
import server.web.casa.app.property.domain.model.PropertyImageRoom
import server.web.casa.app.property.domain.model.request.PropertyRequest
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.user.application.UserService
import server.web.casa.route.property.PropertyRoute

const val ROUTE_PROPERTY = PropertyRoute.PROPERTY

@Tag(name = "Property", description = "Gestion des propriètés")
@RestController
@RequestMapping(ROUTE_PROPERTY)
class PropertyController(
    private val service : PropertyService,
    private val propertyTypeService: PropertyTypeService,
    private val cityService: CityService,
    private val userService: UserService,
    private val communeService: CommuneService,
    private val propertyImageService: PropertyImageService,
    private val propertyImageLivingRoomService: PropertyImageLivingRoomService,
    private val propertyImageRoomService: PropertyImageRoomService,
    private val propertyImageKitchenService: PropertyImageKitchenService,
    private val featureService: FeatureService,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createProperty(
        @Valid @RequestBody request: PropertyRequest
    ): ResponseEntity<out Map<String, Any>> {
       val imageList = request.propertyImage
       val imageKitchen = request.propertyImageKitchen
       val imageRoom = request.propertyImageRoom
       val imageLivingRoom = request.propertyImageLivingRoom
       val city = cityService.findByIdCity(request.cityId)
       val user = userService.findIdUser(request.userId)
       val propertyType = propertyTypeService.findByIdPropertyType(request.propertyTypeId)
       val commune = communeService.findByIdCommune(request.communeId)

        if (city != null && user != null && propertyType != null){
            val property = Property(
                title = request.title,
                description = request.description,
                price = request.price,
                surface = request.surface,
                rooms = request.rooms,
                bedrooms = request.bedrooms,
                kitchen = request.kitchen,
                livingRoom = request.livingRoom,
                bathroom = request.bathroom,
                floor = request.floor,
                address = request.address,
                city = city,
                postalCode = request.postalCode,
                commune = commune,
                features = request.features,
                quartier = request.quartier,
                sold = request.sold,
                transactionType = request.transactionType,
                propertyType = propertyType,
                user = user,
                latitude = request.latitude,
                longitude = request.longitude
            )
            val result = service.create(property)
            val  propertyInstance = service.findByIdProperty(result.propertyId)
            log.info("propertyInstance => ***${propertyInstance}***")
            if (request.propertyImage.isNotEmpty()){
                request.propertyImage.map {
                    val result = propertyImageService.create(PropertyImage(
                        property = propertyInstance,
                        name = it?.image!!
                    ))
                    log.info("test => ***${result}***")
                }
            }
            if (request.propertyImageRoom.isNotEmpty()){
                request.propertyImageRoom.map {
                    val result = propertyImageRoomService.create(PropertyImageRoom(
                        property = propertyInstance,
                        name = it?.image!!
                    ))
                    log.info("test => ***${result}***")
                }
            }

            if (request.propertyImageLivingRoom.isNotEmpty()){
                request.propertyImageLivingRoom.map {
                    val result = propertyImageLivingRoomService.create(PropertyImageLivingRoom(
                        property = propertyInstance,
                        name = it?.image!!
                    ))
                    log.info("test => ***${result}***")
                }
            }

            if (request.propertyImageKitchen.isNotEmpty()){
                request.propertyImageKitchen.map {
                    val result = propertyImageKitchenService.create(PropertyImageKitchen(
                        property = propertyInstance,
                        name = it?.image!!
                    ))
                    log.info("test => ***${result}***")
                }
            }

            val response = mapOf(
                "message" to "Enregistrement réussie pour la proprièté",
                "properties" to result
            )
            return ResponseEntity.status(201).body(response)
        }
        val response = mapOf(
            "message" to "erreur au niveau de la validation"
        )
        return ResponseEntity.badRequest().body(response)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllProperty(): ResponseEntity<Map<String, List<Property>>> {
        val data = service.getAll()
        val response = mapOf("properties" to data)
        return ResponseEntity.ok().body(response)
    }
}