package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.actor.infrastructure.persistence.entity.TypeCardEntity
import server.web.casa.app.actor.infrastructure.persistence.repository.TypeCardRepository
import server.web.casa.route.utils.CardTypeScope
import server.web.casa.utils.ApiResponse
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@RestController
@RequestMapping("api")
@Profile("dev")
class TypeCardController(
    private val repository: TypeCardRepository,
    private val sentry: SentryService,
) {
    @Operation(summary = "Liste de Type card")
    @GetMapping("/{version}/${CardTypeScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllTypeCard(request: HttpServletRequest): ApiResponse<List<TypeCardEntity>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            ApiResponse(repository.findAll().toList())
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.typecard.getalltypecard.count",
                    distributionName = "api.typecard.getalltypecard.latency"
                )
            )
        }
    }
}