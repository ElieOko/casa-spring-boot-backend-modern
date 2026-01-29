package server.web.casa.app.notification.infrastructure.controller.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.coroutineScope
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.notification.application.service.NotificationSystemService
import server.web.casa.route.utils.NotificationScope
import server.web.casa.utils.ApiResponse
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Notification", description = "")
@RestController
@RequestMapping("api")
class NotificationRestController(
    private val service: NotificationSystemService,
    private val sentry: SentryService,
) {
    @Operation(summary = "List notifications of user")
    @GetMapping("/{version}/${NotificationScope.PROTECTED}/owner/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllNotificationByUser(
        request: HttpServletRequest,
        @PathVariable("userId") userId : Long,
    )= coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val data = service.notificationByUser(userId)
            ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.notificationrest.getallnotificationbyuser.count",
                    distributionName = "api.notificationrest.getallnotificationbyuser.latency"
                )
            )
        }
    }

    @Operation(summary = "Desactivate notification")
    @GetMapping("/{version}/${NotificationScope.PRIVATE}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun disableNotification(
        request: HttpServletRequest,
        @PathVariable("id") id : Long,
    )= coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            service.notificationDisable(id)
            ResponseEntity.status(HttpStatus.CREATED).body(mapOf("message" to "cette notification a été supprimer avec succès")).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.notificationrest.disablenotification.count",
                    distributionName = "api.notificationrest.disablenotification.latency"
                )
            )
        }
    }

//    @Operation(summary = "Desactivate notification")
//    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
//    suspend fun createNotification() = coroutineScope {
//
//    }
}