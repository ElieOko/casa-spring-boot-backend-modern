package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.address.application.service.*
import server.web.casa.app.property.application.service.*
import server.web.casa.app.property.domain.model.*
import server.web.casa.app.property.domain.model.filter.PropertyFilter
import server.web.casa.app.property.domain.model.request.PropertyRequest
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.property.PropertyRoute
import java.time.LocalDate

const val ROUTE_PROPERTY = PropertyRoute.PROPERTY
const val ROUTE_PROPERTY_FILTER = PropertyRoute.PROPERTY_FILTER
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
    private val quartierService: QuartierService,
//    private val authSession : Auth
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createProperty(
        @Valid @RequestBody request: PropertyRequest,
        requestHttp: HttpServletRequest
    ): ResponseEntity<out Map<String, Any>> {
//       val ownerId = SecurityContextHolder.getContext().authentication!!.principal as String
       val imageList = request.propertyImage
       val imageKitchen = request.propertyImageKitchen
       val imageRoom = request.propertyImageRoom
       val imageLivingRoom = request.propertyImageLivingRoom
       val city = cityService.findByIdCity(request.cityId)
       val user = userService.findIdUser(request.userId)
       val propertyType = propertyTypeService.findByIdPropertyType(request.propertyTypeId)
       val commune = communeService.findByIdCommune(request.communeId)
       val quartier =  quartierService.findByIdQuartier(request.quartierId)
       val isProd = true
       val baseUrl = if (isProd) "${requestHttp.scheme}://${requestHttp.serverName}"  else  "${requestHttp.scheme}://${requestHttp.serverName}:${requestHttp.serverPort}"
        if (user != null && propertyType != null && imageList.isNotEmpty()){
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
                guarantee = request.guarantee,
                water = request.water,
                electric = request.electric,
                countryValue = request.countryValue,
                communeValue = request.communeValue,
                quartierValue = request.quartierValue,
                cityValue = request.cityValue,
                address = request.address,
                city = city,
                postalCode = request.postalCode,
                commune = commune,
                features = request.features,
                quartier = quartier,
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
            if (imageList.isNotEmpty()){
                imageList.map {
                     propertyImageService.create(PropertyImage(
                        property = propertyInstance.first,
                        name = it?.image!!
                    ),baseUrl)
                }
            }
            if (imageRoom.isNotEmpty()){

                imageRoom.map {
                    propertyImageRoomService.create(PropertyImageRoom(
                        property = propertyInstance.first,
                        name = it?.image!!
                    ),baseUrl)
                }
            }

            if (imageLivingRoom.isNotEmpty()){
                imageLivingRoom.map {
                    val result = propertyImageLivingRoomService.create(PropertyImageLivingRoom(
                        property = propertyInstance.first,
                        name = it?.image!!
                    ),baseUrl)
                    log.info("test => ***${result}***")
                }
            }

            if (imageKitchen.isNotEmpty()){
                imageKitchen.map {
                    propertyImageKitchenService.create(PropertyImageKitchen(
                        property = propertyInstance.first,
                        name = it?.image!!
                    ),baseUrl)
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

    @Operation(summary = "Voir les Property")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllProperty(): ResponseEntity<Map<String, Page<Property>>> {
       val page = 0
       val size = 15
       val sortBy = "title"
       val sortOrder = "asc"
       val data = service.getAll(
           page = page,
           size = size,
           sortBy = sortBy,
           sortOrder = sortOrder
       )
       val response = mapOf("properties" to data)
        return ResponseEntity.ok().body(response)
    }

    @Operation(summary = "Get Property by User")
    @GetMapping("/owner/{userId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPropertyByUser(
        @PathVariable("userId") userId : Long,
    ): ResponseEntity<Map<String, List<Property>>> {
        val data = service.getAllPropertyByUser(userId)
        val response = mapOf("properties" to data)
        return ResponseEntity.ok().body(response)
    }

    @Operation(summary = "Get Property by ID")
    @GetMapping("/{propertyId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPropertyByID(
        @PathVariable("propertyId") propertyId : Long,
    ): ResponseEntity<Map<String, Any>> {
        val data = service.findByIdProperty(propertyId)
        val response = mapOf(
            "properties" to data.first,
            "similaires" to data.second
        )
        return ResponseEntity.ok().body(response)
    }

    @GetMapping(ROUTE_PROPERTY_FILTER,produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Get property by sold into price, room, city, commune with pagination and sorting")
    suspend fun getAllPropertyFilter(
        @Parameter(description = "Transaction Type") @RequestParam transactionType : String,
        @Parameter(description = "Prix minimun") @RequestParam minPrice : Double,
        @Parameter(description = "Prix maximun") @RequestParam maxPrice : Double,
        @Parameter(description = "Type de maison") @RequestParam typeMaison : Long,
        @Parameter(description = "Ville") @RequestParam city : Long,
        @Parameter(description = "Commune") @RequestParam commune : Long,
        @Parameter(description = "Chambre") @RequestParam room : Int,
        @Parameter(description = "Page number(0-based)") @RequestParam(defaultValue = "0") page : Int,
        @Parameter(description = "Page size") @RequestParam(defaultValue = "20") size : Int,
        @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") sortBy : String,
        @Parameter(description = "Sort order (asc/desc)") @RequestParam(defaultValue = "asc") sortOrder : String
    ): ResponseEntity<Map<String, Page<Property>>> {
        val data = service.filterProduct(
            filterModel = PropertyFilter(
                transactionType = transactionType,
                minPrice = minPrice,
                maxPrice = maxPrice,
                city = city,
                commune = commune,
                typeMaison = typeMaison,
                room = room
            ),
            page = page,
            size = size,
            sortBy = sortBy,
            sortOrder = sortOrder
        )
        val response = mapOf("properties" to data)
        return ResponseEntity.ok().body(response)
    }

    @Operation(summary = "Modification Property")
    @PutMapping("/owner/{userId}/{propertyId}")
    suspend fun updateProperty(
        @PathVariable("userId") userId : Long,
        @PathVariable("propertyId") propertyId : Long,
        @Valid @RequestBody request: PropertyRequest
    ): ResponseEntity<Property> {
        val city = cityService.findByIdCity(request.cityId)
        val user = userService.findIdUser(request.userId)
        val propertyType = propertyTypeService.findByIdPropertyType(request.propertyTypeId)
        val commune = communeService.findByIdCommune(request.communeId)
        val quartier =  quartierService.findByIdQuartier(request.quartierId)
        val property = service.findByIdProperty(propertyId)
        if (property.first.user?.userId != userId){
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Cet auteur n'appartient pas à la proprièté.")
        }
        if (propertyType != null) {
            property.first.propertyType = propertyType
        }
        property.first.title = request.title
        property.first.description = request.description
        property.first.address = request.address
        property.first.bathroom = request.bathroom
        property.first.rooms = request.rooms
        property.first.countryValue = request.countryValue
        property.first.sold = request.sold
        property.first.kitchen = request.kitchen
        property.first.cityValue = request.cityValue
        property.first.communeValue = request.communeValue
        property.first.electric = request.electric
        property.first.water = request.water
        property.first.guarantee = request.guarantee
        property.first.city = city
        property.first.quartier = quartier
        property.first.commune = commune
        property.first.price = request.price
        property.first.floor = request.floor
        property.first.quartierValue = request.quartierValue
        property.first.transactionType = request.transactionType
        property.first.postalCode = request.postalCode
        property.first.surface = request.surface
        property.first.latitude = request.latitude
        property.first.longitude = request.longitude
        property.first.updatedAt = LocalDate.now()
        val updated = service.create(property.first)
        return ResponseEntity.ok(updated)
    }
}