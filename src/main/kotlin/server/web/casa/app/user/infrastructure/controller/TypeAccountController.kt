package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import server.web.casa.app.user.application.service.TypeAccountService
import server.web.casa.app.user.domain.model.TypeAccount
import server.web.casa.route.account.AccountTypeScope
import server.web.casa.utils.ApiResponse
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@RestController
@RequestMapping("api")
@Profile("dev")
class TypeAccountController(
    private val service: TypeAccountService,
    private val sentry: SentryService,
) {
    @Operation(summary = "Liste de Type Accounts")
    @GetMapping("/{version}/${AccountTypeScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllTypeAccountE(request: HttpServletRequest): ApiResponse<List<TypeAccount>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            ApiResponse(service.getAll().toList())
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.typeaccount.getalltypeaccounte.count",
                    distributionName = "api.typeaccount.getalltypeaccounte.latency"
                )
            )
        }
    }
}