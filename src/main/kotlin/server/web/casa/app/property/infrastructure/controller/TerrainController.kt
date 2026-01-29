package server.web.casa.app.property.infrastructure.controller

import server.web.casa.route.GlobalRoute
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.address.application.service.*
import server.web.casa.app.payment.application.service.*
import server.web.casa.app.property.application.service.*
import server.web.casa.app.property.domain.model.*
import server.web.casa.app.property.domain.model.request.TerrainRequest
import server.web.casa.app.property.domain.model.request.toDomain
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.property.PropertyTerrainScope
import server.web.casa.utils.*
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Terrain", description = "")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
class TerrainController(
    private val service : TerrainService,
    private val devise : DeviseService,
    private val userService: UserService,
    private val imageTerrain: TerrainImageService,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService,
    private val propertyTypeService: PropertyTypeService,
    private val sentry: SentryService,
) {
    @Operation(summary = "Création Terrain")
    @PostMapping("/${PropertyTerrainScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createTerrain(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: TerrainRequest,
    ) = coroutineScope{
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            //        propertyTypeService.findByIdPropertyType(request.bureau.propertyTypeId?:0)
                    if (request.propertyTypeId != 9L) throw ResponseStatusException(HttpStatusCode.valueOf(404), "Ce type n'appartient pas au Terrain")
                    val city = if (request.cityId != null) cityService.findByIdCity(request.cityId) else null
                    val commune = communeService.findByIdCommune(request.communeId)
                    val quartier =  if (request.quartierId != null) quartierService.findByIdQuartier(request.quartierId) else null
                    devise.getById(request.deviseId)
                    userService.findIdUser(request.userId)
                    request.cityId =  city?.cityId
                    request.communeId = commune?.communeId
                    request.quartierId = quartier?.quartierId
                    if (request.images.isEmpty()) throw ResponseStatusException(HttpStatusCode.valueOf(404), "Precisez des images.")
                    val data = service.create(request.toDomain())
                    request.images.forEach { imageTerrain.create(ImageRequestStandard(data.id!!,it.image)) }
                    ApiResponseWithMessage(
                        data = data,
                        message = "Enregistrement réussie pour la proprièté terrain",
                    )
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.terrain.createterrain.count",
                    distributionName = "api.terrain.createterrain.latency"
                )
            )
        }
    }

    @Operation(summary = "List des terrain")
    @GetMapping("/${PropertyTerrainScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllTerrain(request: HttpServletRequest) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val data = service.getAll()
            val response = mapOf("terrain" to data)
            ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.terrain.getallterrain.count",
                    distributionName = "api.terrain.getallterrain.latency"
                )
            )
        }
    }
    @Operation(summary = "Sold")
    @PutMapping("/${PropertyTerrainScope.PROTECTED}/sold/{propertyId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun soldOutOrInTerrain(
        httpRequest: HttpServletRequest,
        @PathVariable("propertyId") propertyId : Long,
        @RequestBody request : StatusState
    )= coroutineScope{
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
                    countName = "api.terrain.soldoutorinterrain.count",
                    distributionName = "api.terrain.soldoutorinterrain.latency"
                )
            )
        }
    }

    @Operation(summary = "Enable or disable")
    @PutMapping("/${PropertyTerrainScope.PROTECTED}/enable/{propertyId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun toEnableOrDisableTerrain(
        httpRequest: HttpServletRequest,
        @PathVariable("propertyId") propertyId : Long,
        @RequestBody request : StatusState
    )= coroutineScope{
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
                    countName = "api.terrain.toenableordisableterrain.count",
                    distributionName = "api.terrain.toenableordisableterrain.latency"
                )
            )
        }
    }
    @Operation(summary = "Get Terrain by User")
    @GetMapping("/${PropertyTerrainScope.PROTECTED}/owner/{userId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllTerrainByUser(
        request: HttpServletRequest,
        @PathVariable("userId") userId : Long,
    ) = coroutineScope {
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
                    countName = "api.terrain.getallterrainbyuser.count",
                    distributionName = "api.terrain.getallterrainbyuser.latency"
                )
            )
        }
    }

    @Operation(summary = "Modification Terrain")
    @PutMapping("/${PropertyTerrainScope.PROTECTED}/owner/{terrainId}")
    suspend fun updateTerrain(
        httpRequest: HttpServletRequest,
        @PathVariable("terrainId") terrainId : Long,
        @Valid @RequestBody request: TerrainRequest
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            userService.findIdUser(request.userId)
            val terrain = service.findById(terrainId)
            if (terrain.userId != request.userId) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet utilisateur n'appartient pas à la proprièté terrain.")
            val city = cityService.findByIdCity(request.cityId)
            val propertyType = propertyTypeService.findByIdPropertyType(request.propertyTypeId!!)
            val commune = communeService.findByIdCommune(request.communeId)
            val quartier =  quartierService.findByIdQuartier(request.quartierId)
            val data = request.toDomain()
            data.cityId = city?.cityId
            data.propertyTypeId = propertyType.propertyTypeId
            data.id = terrainId
            data.communeId = commune?.communeId
            data.quartierId = quartier?.quartierId
            service.update(data)
            val message = mutableMapOf("message" to "Modification effectuée avec succès")
            ResponseEntity.ok(message).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.terrain.updateterrain.count",
                    distributionName = "api.terrain.updateterrain.latency"
                )
            )
        }
    }
}