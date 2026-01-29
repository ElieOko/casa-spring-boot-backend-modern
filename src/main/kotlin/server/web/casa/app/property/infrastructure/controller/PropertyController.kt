package server.web.casa.app.property.infrastructure.controller

import server.web.casa.route.GlobalRoute
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.address.application.service.*
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.property.application.service.*
import server.web.casa.app.property.domain.model.*
import server.web.casa.app.property.domain.model.dto.PropertyMasterDTO
import server.web.casa.app.property.domain.model.dto.toDto
import server.web.casa.app.property.domain.model.filter.PropertyFilter
import server.web.casa.app.property.domain.model.request.*
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.property.PropertyScope
import server.web.casa.security.monitoring.MetricModel
import server.web.casa.security.monitoring.SentryService
import server.web.casa.utils.ApiResponse
import server.web.casa.utils.ApiResponseWithMessage
import kotlin.collections.mutableMapOf

@Tag(name = "Property", description = "Gestion des propriètés")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
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
    private val devise : DeviseService,
    private val quartierService: QuartierService,
    private val sentry: SentryService,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    @PostMapping("/${PropertyScope.PRIVATE}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createProperty(
        @Valid @RequestBody request: PropertyRequest,
        requestHttp: HttpServletRequest
    ): ApiResponseWithMessage<PropertyMasterDTO> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
                    //       val ownerId = SecurityContextHolder.getContext().authentication!!.principal as String
                    devise.getById(request.deviseId)
                    val imageList = request.propertyImage
                    val imageKitchen = request.propertyImageKitchen
                    val imageRoom = request.propertyImageRoom
                    val imageLivingRoom = request.propertyImageLivingRoom
                    val city = if (request.cityId != null) cityService.findByIdCity(request.cityId) else null
                    val user = userService.findIdUser(request.userId)
                    val propertyType = propertyTypeService.findByIdPropertyType(request.propertyTypeId)
                    val commune = communeService.findByIdCommune(request.communeId)
                    val quartier =  if (request.quartierId != null) quartierService.findByIdQuartier(request.quartierId) else null
                    val isProd = true
                    val baseUrl = if (isProd) "${requestHttp.scheme}://${requestHttp.serverName}"  else  "${requestHttp.scheme}://${requestHttp.serverName}:${requestHttp.serverPort}"
                    if (imageList.isEmpty()) throw ResponseStatusException(
                        HttpStatusCode.valueOf(404),
                        "Precisez des images."
                    )
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
                        city = city?.cityId,
                        postalCode = request.postalCode,
                        commune = commune?.communeId,
                        quartier = quartier?.quartierId,
                        sold = request.sold,
                        transactionType = request.transactionType,
                        user = user.userId,
                        latitude = request.latitude,
                        longitude = request.longitude,
                        propertyTypeId = propertyType.propertyTypeId,
                        deviseId = request.deviseId
                    )
                    val result = service.create(property.toDto(), request.features)
                    val propertyInstance = service.findByIdProperty(result.property.propertyId!!)
                    log.info("propertyInstance => ***${propertyInstance}***")
                    if (imageList.isNotEmpty()){
                        imageList.forEach {
                            propertyImageService.create(PropertyImage(
                                propertyId = propertyInstance.first.property.propertyId,
                                name = it?.image!!
                            ),baseUrl)
                        }
                    }
                    if (imageRoom.isNotEmpty()){
                        imageRoom.forEach {
                            propertyImageRoomService.create(PropertyImageRoom(
                                propertyId = propertyInstance.first.property.propertyId,
                                name = it?.image!!
                            ),baseUrl)
                        }
                    }
                    if (imageLivingRoom.isNotEmpty()){
                        imageLivingRoom.forEach {
                            val result = propertyImageLivingRoomService.create(PropertyImageLivingRoom(
                                propertyId = propertyInstance.first.property.propertyId,
                                name = it?.image!!
                            ),baseUrl)
                            log.info("test => ***${result}***")
                        }
                    }
                    if (imageKitchen.isNotEmpty()){
                        imageKitchen.forEach {
                            propertyImageKitchenService.create(PropertyImageKitchen(
                                propertyId = propertyInstance.first.property.propertyId,
                                name = it?.image!!
                            ),baseUrl)
                        }
                    }
                    ApiResponseWithMessage(
                        data = result,
                        message = "Enregistrement réussie pour la proprièté",
                    )

            //        throw Exception("")
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${requestHttp.method} /${requestHttp.requestURI}",
                    countName = "api.property.createproperty.count",
                    distributionName = "api.property.createproperty.latency"
                )
            )
        }
    }

    @Operation(summary = "Voir les Property")
    @GetMapping("/${PropertyScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllProperty(
        request: HttpServletRequest
    ): ResponseEntity<Map<String, List<PropertyMasterDTO>>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val page = 0
            val size = 15
            val sortBy = "title"
            val sortOrder = "asc"
            val data = service.getAll(
                page = page,
                size = size,
                sortBy = sortBy,
                sortOrder = sortOrder
            ).toList()
             val userAgent = request.getHeader("User-Agent")
            val deviceBrand = request.getHeader("X-Device-Brand")
            val deviceModel = request.getHeader("X-Device-Model")
            val os = request.getHeader("X-OS")
            val osVersion = request.getHeader("X-OS-Version")
            log.info("Agent :$userAgent\ndevice:$deviceBrand\nos:$os")
            val response = mapOf("properties" to data)
            ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.property.getallproperty.count",
                    distributionName = "api.property.getallproperty.latency"
                )
            )
        }
    }

    @Operation(summary = "Get Property by User")
    @GetMapping("/${PropertyScope.PRIVATE}/owner/{userId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPropertyByUser(
        request: HttpServletRequest,
        @PathVariable("userId") userId : Long,
    ): ApiResponse<List<PropertyMasterDTO>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val data = service.getAllPropertyByUser(userId)
             ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.property.getallpropertybyuser.count",
                    distributionName = "api.property.getallpropertybyuser.latency"
                )
            )
        }
    }

    @Operation(summary = "Get Property by ID")
    @GetMapping("/${PropertyScope.PUBLIC}/{propertyId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPropertyByID(
        request: HttpServletRequest,
        @PathVariable("propertyId") propertyId : Long,
    ): ResponseEntity<Map<String, Any>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val data = service.findByIdProperty(propertyId)
            val response = mapOf(
                "properties" to data.first,
                "similaires" to data.second
            )
            return ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.property.getallpropertybyid.count",
                    distributionName = "api.property.getallpropertybyid.latency"
                )
            )
        }
    }

    @GetMapping( "/${PropertyScope.PUBLIC}/filter",produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Get property by sold into price, room, city, commune with pagination and sorting")
    suspend fun getAllPropertyFilter(
        request: HttpServletRequest,
        @Parameter(description = "Transaction Type") @RequestParam transactionType : String,
        @Parameter(description = "Prix minimun") @RequestParam minPrice : Double,
        @Parameter(description = "Prix maximun") @RequestParam maxPrice : Double,
        @Parameter(description = "Type de maison") @RequestParam typeMaison : Long,
        @Parameter(description = "Ville") @RequestParam city : Long?,
        @Parameter(description = "Commune") @RequestParam commune : Long?,
        @Parameter(description = "Ville value") @RequestParam cityValue : String?,
        @Parameter(description = "Commune value") @RequestParam communeValue : String?,
        @Parameter(description = "Chambre") @RequestParam room : Int,
        @Parameter(description = "Page number(0-based)") @RequestParam(defaultValue = "0") page : Int,
        @Parameter(description = "Page size") @RequestParam(defaultValue = "20") size : Int,
        @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") sortBy : String,
        @Parameter(description = "Sort order (asc/desc)") @RequestParam(defaultValue = "asc") sortOrder : String
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val data = service.filterProduct(
                filterModel = PropertyFilter(
                    transactionType = transactionType,
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    city = city,
                    commune = commune,
                    typeMaison = typeMaison,
                    room = room,
                    cityValue = cityValue,
                    communeValue = communeValue,
                ),
                page = page,
                size = size,
                sortBy = sortBy,
                sortOrder = sortOrder
            )
            val response = mapOf("properties" to data)
             ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.property.getallpropertyfilter.count",
                    distributionName = "api.property.getallpropertyfilter.latency"
                )
            )
        }
    }

    @Operation(summary = "Modification Property")
    @PutMapping("/${PropertyScope.PROTECTED}/owner/{userId}/{propertyId}")
    suspend fun updateProperty(
        httpRequest: HttpServletRequest,
        @PathVariable("userId") userId : Long,
        @PathVariable("propertyId") propertyId : Long,
        @Valid @RequestBody request: PropertyRequest
    ): ResponseEntity<PropertyMasterDTO> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val city = cityService.findByIdCity(request.cityId)
            userService.findIdUser(request.userId)
            val propertyType = propertyTypeService.findByIdPropertyType(request.propertyTypeId)
            val commune = communeService.findByIdCommune(request.communeId)
            val quartier =  quartierService.findByIdQuartier(request.quartierId)
            val property = service.findByIdProperty(propertyId)
            if (property.first.property.userId != userId) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet utilisateur n'appartient pas à la proprièté.")
            property.first.typeProperty.propertyTypeId = propertyType.propertyTypeId
            property.first.property.title = request.title
            property.first.property.description = request.description
            property.first.address.address = request.address
            property.first.property.bathroom = request.bathroom
            property.first.property.rooms = request.rooms
            property.first.address.countryValue = request.countryValue
            property.first.property.sold = request.sold
            property.first.property.kitchen = request.kitchen
            property.first.address.cityValue = request.cityValue
            property.first.address.communeValue = request.communeValue
            property.first.property.electric = request.electric
            property.first.property.water = request.water
            property.first.property.guarantee = request.guarantee
            property.first.localAddress.city?.cityId = city?.cityId
            property.first.localAddress.quartier?.quartierId = quartier?.quartierId
            property.first.localAddress.commune?.communeId = commune?.communeId
            property.first.property.price = request.price
            property.first.property.floor = request.floor
            property.first.address.quartierValue = request.quartierValue
            property.first.property.transactionType = request.transactionType
            property.first.address.postalCode = request.postalCode
            property.first.property.surface = request.surface
            property.first.geoZone.lat = request.latitude
            property.first.geoZone.lng = request.longitude
            val updated = service.update(property.first)
            return ResponseEntity.ok(updated).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.property.updateproperty.count",
                    distributionName = "api.property.updateproperty.latency"
                )
            )
        }
    }

    @Operation(summary = "Modification Property")
    @PutMapping("/${PropertyScope.PROTECTED}/image/{propertyId}")
    suspend fun updateFile(
        httpRequest: HttpServletRequest,
        @PathVariable("propertyId") propertyId : Long,
        @Valid @RequestBody request: PropertyImageChangeRequest
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            service.findByIdProperty(propertyId)
            val result = if (request.propertyImage.isNotEmpty()) propertyImageService.updateFile(propertyId,request.propertyImage) else false
            val result2 = if (request.propertyImageRoom.isNotEmpty()) propertyImageRoomService.updateFile(propertyId,request.propertyImageRoom) else false
            val result3 = if (request.propertyImageKitchen.isNotEmpty()) propertyImageKitchenService.updateFile(propertyId,request.propertyImageKitchen) else false
            val result4 = if (request.propertyImageLivingRoom.isNotEmpty()) propertyImageLivingRoomService.updateFile(propertyId,request.propertyImageLivingRoom) else false
            val message = mutableMapOf("message" to "Modification effectuée avec succès")
            if (result || result4 || result3 || result2)  ResponseEntity.ok(message).also { statusCode = it.statusCode.value().toString() } else {
                message["message"] = "Aucune modification n'a été effectuée"
                ResponseEntity.badRequest().body(message).also { statusCode = it.statusCode.value().toString() }
            }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.property.updatefile.count",
                    distributionName = "api.property.updatefile.latency"
                )
            )
        }
    }

    @Operation(summary = "Suppression Property")
    @DeleteMapping("/${PropertyScope.PROTECTED}/image/{propertyId}")
    suspend fun deleteFile(
        httpRequest: HttpServletRequest,
        @PathVariable("propertyId") propertyId : Long,
        @Valid @RequestBody request: PropertyImagesRequest
    ) = coroutineScope{
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            service.findByIdProperty(propertyId)
            val result = if (request.propertyImage.isNotEmpty()) propertyImageService.deleteFile(propertyId,request.propertyImage) else false
            val result2 = if (request.propertyImageRoom.isNotEmpty()) propertyImageRoomService.deleteFile(propertyId,request.propertyImageRoom) else false
            val result3 = if (request.propertyImageKitchen.isNotEmpty()) propertyImageKitchenService.deleteFile(propertyId,request.propertyImageKitchen) else false
            val result4 = if (request.propertyImageLivingRoom.isNotEmpty()) propertyImageLivingRoomService.deleteFile(propertyId,request.propertyImageLivingRoom) else false
            val message = mutableMapOf("message" to "Suppression effectuée avec succès")
            if (result || result4 || result3 || result2)  ResponseEntity.ok(message).also { statusCode = it.statusCode.value().toString() } else {
                message["message"] = "Aucune suppression n'a été effectuée"
                ResponseEntity.badRequest().body(message).also { statusCode = it.statusCode.value().toString() }
            }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.property.deletefile.count",
                    distributionName = "api.property.deletefile.latency"
                )
            )
        }
    }

    @Operation(summary = "Sold")
    @PutMapping("/${PropertyScope.PROTECTED}/sold/{propertyId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun soldOutOrIn(
        httpRequest: HttpServletRequest,
        @PathVariable("propertyId") propertyId : Long,
        @RequestBody request : StatusState
    ) = coroutineScope{
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val message = mutableMapOf("message" to if(request.status) "Proprièté bouqué(soldout) avec succès" else "Proprièté non bouqué(soldin) avec succès")
            val data = service.findById(propertyId)
            data.sold = request.status
            service.createOrUpdate(data)
            ResponseEntity.badRequest().body(message).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.property.soldoutorin.count",
                    distributionName = "api.property.soldoutorin.latency"
                )
            )
        }
    }

    @Operation(summary = "Enable or disable")
    @PutMapping("/${PropertyScope.PROTECTED}/enable/{propertyId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun toEnableOrDisable(
        httpRequest: HttpServletRequest,
        @PathVariable("propertyId") propertyId : Long,
        @RequestBody request : StatusState
    ) = coroutineScope{
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val message = mutableMapOf("message" to if(request.status) "Proprièté activé avec succès" else "Proprièté desactivé avec succès")
            val data= service.findById(propertyId)
            data.isAvailable = request.status
            service.createOrUpdate(data)
            ResponseEntity.badRequest().body(message).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.property.toenableordisable.count",
                    distributionName = "api.property.toenableordisable.latency"
                )
            )
        }
    }

}
