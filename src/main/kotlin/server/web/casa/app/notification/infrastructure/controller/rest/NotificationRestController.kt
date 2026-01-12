package server.web.casa.app.notification.infrastructure.controller.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.notification.application.service.NotificationSystemService
import server.web.casa.route.utils.AgenceRoute.NOTIFICATION
import server.web.casa.utils.ApiResponse

@Tag(name = "Notification", description = "")
@RestController
@RequestMapping(NOTIFICATION)
class NotificationRestController(
    private val service: NotificationSystemService,
) {
    @Operation(summary = "List notifications of user")
    @GetMapping("/owner/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllNotificationByUser(
        @PathVariable("userId") userId : Long,
    )= coroutineScope {
        val data = service.notificationByUser(userId)
        ApiResponse(data)
    }

    @Operation(summary = "Desactivate notification")
    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun disableNotification(
        @PathVariable("id") id : Long,
    )= coroutineScope {
        service.notificationDisable(id)
        ResponseEntity.status(HttpStatus.CREATED).body(mapOf("message" to "cette notification a été supprimer avec succès"))
    }
}