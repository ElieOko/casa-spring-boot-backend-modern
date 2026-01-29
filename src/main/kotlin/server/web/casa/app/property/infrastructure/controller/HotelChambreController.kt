package server.web.casa.app.property.infrastructure.controller

import server.web.casa.route.GlobalRoute
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.property.application.service.*
import server.web.casa.app.property.domain.model.*
import server.web.casa.route.property.PropertyHotelChambreScope
import server.web.casa.utils.*
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Hotel Chambre", description = "")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
class HotelChambreController(
    private val service: HotelChambreService,
    private val hotel: HotelService,
    private val imageService: HotelChambreImageService,
    private val sentry: SentryService,
) {
    @Operation(summary = "Création Hotel Chambre")
    @PostMapping("/${PropertyHotelChambreScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createHotelChambre(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: HotelChambreRequest,
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            if (request.userId == null) ResponseEntity.badRequest().body("User ID must not be null!").also { statusCode = it.statusCode.value().toString() }
            if (request.images.size < 3) ResponseEntity.badRequest().body("Vous devez fournir au minimun 3 images pour votre chambres").also { statusCode = it.statusCode.value().toString() }
            val hotel = hotel.getAllByUser(request.userId!!)
            if (hotel.isEmpty()) ResponseEntity.badRequest().body("Vous devez avoir une hotel au minimum un pour poster un vos chambres").also { statusCode = it.statusCode.value().toString() }
            request.hotelId = hotel.first().hotel.id
            val data = service.create(request.toDomain())
            request.images.forEach { imageService.create(ImageRequestStandard(data.id!!,it.image)) }
            ApiResponseWithMessage(
                data = data,
                message = "Enregistrement réussie pour votre chambre",
            )
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.hotelchambre.createhotelchambre.count",
                    distributionName = "api.hotelchambre.createhotelchambre.latency"
                )
            )
        }
    }

    @Operation(summary = "List des chambres")
    @GetMapping("/${PropertyHotelChambreScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllHotelChambre(request: HttpServletRequest) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            ApiResponse(service.getAll())
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.hotelchambre.getallhotelchambre.count",
                    distributionName = "api.hotelchambre.getallhotelchambre.latency"
                )
            )
        }
    }

}