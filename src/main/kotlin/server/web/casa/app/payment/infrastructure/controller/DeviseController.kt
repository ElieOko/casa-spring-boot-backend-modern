package server.web.casa.app.payment.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.coroutineScope
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.route.utils.AgenceFeature.DEVISE
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@RestController
@RequestMapping("api")
@Profile("dev")
class DeviseController(
    private val service: DeviseService,
    private val sentry: SentryService,
) {
    @Operation(summary = "Liste des devises")
    @GetMapping("/{version}/public/devises",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllAccountE(request: HttpServletRequest): Map<String, List<Devise>>  = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = service.getAllData().toList()
            mapOf("devises" to data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.devise.getallaccounte.count",
                    distributionName = "api.devise.getallaccounte.latency"
                )
            )
        }
    }
}