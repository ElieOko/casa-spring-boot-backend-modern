package server.web.casa.app.address.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.address.application.service.CityService
import server.web.casa.app.address.domain.model.City
import server.web.casa.route.address.CityScope
import server.web.casa.utils.Mode
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "City", description = "Gestion des villes")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)
class CityController(
   private val service : CityService,
   private val sentry: SentryService,
) {
    @Operation(summary = "Liste de villes")
    @GetMapping("/{version}/${CityScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllCity(request: HttpServletRequest): ResponseEntity<Map<String, List<City?>>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val data = service.findAllCity()
            val response = mapOf("cities" to data)
            return ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.city.getallcity.count",
                    distributionName = "api.city.getallcity.latency"
                )
            )
        }
    }
}