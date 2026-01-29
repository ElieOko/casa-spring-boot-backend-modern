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

    private val sentry: SentryService,
) {
    @Operation(summary = "Création Hotel")
    @PostMapping("/${PropertyHotelScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createHotel(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: HotelRequest,
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            if (request.propertyTypeId != 5L) throw ResponseStatusException(HttpStatusCode.valueOf(404), "Ce type n'appartient pas au hotel")
            val city = if (request.cityId != null) cityService.findByIdCity(request.cityId) else null
            val commune = communeService.findByIdCommune(request.communeId)
            val quartier =  if (request.quartierId != null) quartierService.findByIdQuartier(request.quartierId) else null
            userService.findIdUser(request.userId!!)
            request.cityId =  city?.cityId
            request.communeId = commune?.communeId
            request.quartierId = quartier?.quartierId
            val result = service.save(request.toDomain())
            ApiResponseWithMessage(message = "Enregistrement réussie pour votre hotel ${result.title}", data = result)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
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
        var statusCode = "200"
        try {
            val data = service.getAllHotel()
            ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
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
        var statusCode = "200"
        try {
            val data = service.getAllByUser(userId)
            ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.hotel.getallhotelbyuser.count",
                    distributionName = "api.hotel.getallhotelbyuser.latency"
                )
            )
        }
    }
}