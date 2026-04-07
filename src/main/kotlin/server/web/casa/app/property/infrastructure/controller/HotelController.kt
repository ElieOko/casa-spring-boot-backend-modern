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
import server.web.casa.app.property.application.service.*
import server.web.casa.app.property.domain.model.*
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.property.PropertyHotelScope
import server.web.casa.utils.*
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.Auth
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Hotel", description = "")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
class HotelController(
    private val service: HotelService,
    private val userService: UserService,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService,
    private val propertyTypeService: PropertyTypeService,
    private val auth : Auth,
    private val sentry: SentryService,
) {
    @Operation(summary = "Création Hotel")
    @PostMapping("/${PropertyHotelScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createHotel(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: HotelRequest,
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        val userConnect = auth.user()
        try {
            if (userConnect?.first?.isCertified != true) throw ResponseStatusException(HttpStatusCode.valueOf(403), MessageResponse.ACCOUNT_NOT_CERTIFIED)

            if (request.propertyTypeId != 5L) throw ResponseStatusException(HttpStatusCode.valueOf(404), "Ce type n'appartient pas au hotel")
            val city = if (request.cityId != null) cityService.findByIdCity(request.cityId) else null
            val commune = communeService.findByIdCommune(request.communeId)
            val quartier =  if (request.quartierId != null) quartierService.findByIdQuartier(request.quartierId) else null
            userService.findIdUser(request.userId!!)
            request.cityId =  city?.cityId
            request.communeId = commune?.communeId
            request.quartierId = quartier?.quartierId
            val result = service.save(request.toDomain())
            request.images.forEach { service.createFile(ImageRequestStandard(result.id!!,it.image)) }
            ApiResponseWithMessage(message = "Enregistrement réussie pour votre hotel ${result.title}", data = result)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.hotel.createhotel.count",
                    distributionName = "api.hotel.createhotel.latency"
                )
            )
        }
    }

    @Operation(summary = "List des hotels")
    @GetMapping("/${PropertyHotelScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllHotel(request: HttpServletRequest) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = service.getAllHotel()
            ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.hotel.getallhotel.count",
                    distributionName = "api.hotel.getallhotel.latency"
                )
            )
        }
    }

    @Operation(summary = "List des hotels protected")
    @GetMapping("/${PropertyHotelScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllHotelProtect(request: HttpServletRequest) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val session = auth.user()
            val state: Boolean? = session?.second?.find{ true }
            when (state) {
                true -> {
                    val data = service.getAllHotel(true)
                    ResponseEntity.ok().body(data)}
                false,null ->{
                    ResponseEntity.status(403).body(mapOf("message" to "Accès non autorisé"))}
            }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.hotel.getallhotel.count",
                    distributionName = "api.hotel.getallhotel.latency"
                )
            )
        }
    }

    @Operation(summary = "List des hotels")
    @GetMapping("/${PropertyHotelScope.PRIVATE}/owner/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllHotelByUser(
        request: HttpServletRequest,
        @PathVariable("userId") userId : Long,
    )= coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = service.getAllByUser(userId)
            ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.hotel.getallhotelbyuser.count",
                    distributionName = "api.hotel.getallhotelbyuser.latency"
                )
            )
        }
    }

    @Operation(summary = "Get Hotel by ID")
    @GetMapping("/${PropertyHotelScope.PUBLIC}/{hotelId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getHotelByID(
        request: HttpServletRequest,
        @PathVariable("hotelId") hotelId : Long,
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = service.showDetail(hotelId)
            ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.property.getHotelById.count",
                    distributionName = "api.property.getHotelById.latency"
                )
            )
        }
    }

    @Operation(summary = "Get Hotel by ID")
    @GetMapping("/${PropertyHotelScope.PROTECTED}/{hotelId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getHotelByIDProtected(
        request: HttpServletRequest,
        @PathVariable("hotelId") hotelId : Long,
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val session = auth.user()
            val state: Boolean? = session?.second?.find{ true }
            when(state) {
                true -> {
                    val data = service.showDetail(hotelId, false)
                    ApiResponse(data)
                }
                else -> ResponseEntity.status(403).body(mapOf("message" to "Accès non autorisé"))
            }

        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.property.getHotelById.count",
                    distributionName = "api.property.getHotelById.latency"
                )
            )
        }
    }
}