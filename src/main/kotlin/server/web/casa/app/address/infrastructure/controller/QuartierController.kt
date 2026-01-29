package server.web.casa.app.address.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.address.application.service.QuartierService
import server.web.casa.app.address.domain.model.Quartier
import server.web.casa.route.address.QuartierScope
import server.web.casa.utils.Mode
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Quartier", description = "Gestion des quartiers")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)
class QuartierController(
   private val service : QuartierService,
   private val sentry: SentryService,
) {
    @GetMapping("/{version}/${QuartierScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllQuartier(request: HttpServletRequest): ResponseEntity<Map<String, List<Quartier>>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val data = service.findAllQuartier()
            val response = mapOf("quartiers" to data)
            return ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.quartier.getallquartier.count",
                    distributionName = "api.quartier.getallquartier.latency"
                )
            )
        }
    }
}