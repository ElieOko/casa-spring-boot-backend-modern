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
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import server.web.casa.app.payment.domain.model.DeviseLocal
import server.web.casa.security.Auth
import server.web.casa.security.monitoring.MetricModel

@RestController
@RequestMapping("api")
@Profile("dev")
class DeviseController(
    private val service: DeviseService,
    private val sentry: SentryService,
    private val auth: Auth,
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

    @Operation(summary = "Liste des devises")
    @PutMapping("/{version}/protected/devises/taux",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun changeTaux(request: HttpServletRequest,@Valid @RequestBody local : DeviseLocal) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val session = auth.user()
            val state: Boolean? = session?.second?.find{ true }
            when (state) {
                true -> {
                    val data = service.getById(1)
                    data?.tauxLocal = local.tauxLocal
                    mapOf("message" to "Le taux a été mises à jour avec succès.", "stat" to "1$ = ${local.tauxLocal?.times(1)}CDF","stat_" to "10$ = ${local.tauxLocal?.times(10)}CDF")
                }
                false,null -> {
                    ResponseEntity.status(403).body(mapOf("message" to "Accès non autorisé"))}
            }

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