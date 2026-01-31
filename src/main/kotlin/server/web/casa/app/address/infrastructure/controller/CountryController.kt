package server.web.casa.app.address.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.address.application.service.CountryService
import server.web.casa.app.address.domain.model.Country
import server.web.casa.route.address.CountryScope
import server.web.casa.utils.Mode
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Country", description = "Gestion des pays")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)
class CountryController(
   private val service : CountryService,
   private val sentry: SentryService,
) {
    @GetMapping("/{version}/${CountryScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllCountry(request: HttpServletRequest): ResponseEntity<Map<String, List<Country>>> {
        val startNanos = System.nanoTime()
        try {
            val data = service.findAllCountry()
            val response = mapOf("countries" to data)
            return ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.country.getallcountry.count",
                    distributionName = "api.country.getallcountry.latency"
                )
            )
        }
    }
}