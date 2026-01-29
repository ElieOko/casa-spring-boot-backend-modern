package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import server.web.casa.app.property.application.service.AgenceService
import server.web.casa.app.property.domain.model.*
import server.web.casa.route.utils.AgenceScope
import server.web.casa.utils.*
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Agence", description = "")
@RestController
@RequestMapping("api")
class AgenceController(
    private val service: AgenceService,
    private val sentry: SentryService,
) {
    @Operation(summary = "Création agence")
    @PostMapping("/{version}/${AgenceScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createAgence(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: AgenceDTO,
    ): ApiResponseWithMessage<Agence> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val result = service.create(request.toDomain())
            ApiResponseWithMessage(message = "Enregistrement réussie pour votre agence ${result.name}", data = result)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.agence.createagence.count",
                    distributionName = "api.agence.createagence.latency"
                )
            )
        }
    }

    @Operation(summary = "List des agences")
    @GetMapping("/{version}/${AgenceScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllAgence(request: HttpServletRequest) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val data = service.getAllAgence()
            ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.agence.getallagence.count",
                    distributionName = "api.agence.getallagence.latency"
                )
            )
        }
    }

    @Operation(summary = "List des agences")
    @GetMapping("/{version}/${AgenceScope.PROTECTED}/owner/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllAgenceByUser(
        request: HttpServletRequest,
        @PathVariable("userId") userId : Long,
    )= coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            //        val ownerId = SecurityContextHolder.getContext().authentication!!.principal as String
                    val data = service.getAllByUser(userId)
                    ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.agence.getallagencebyuser.count",
                    distributionName = "api.agence.getallagencebyuser.latency"
                )
            )
        }
    }
}