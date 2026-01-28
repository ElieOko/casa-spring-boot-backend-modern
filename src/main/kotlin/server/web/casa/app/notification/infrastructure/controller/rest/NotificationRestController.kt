package server.web.casa.app.notification.infrastructure.controller.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.coroutineScope
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.notification.application.service.NotificationSystemService
import server.web.casa.route.utils.NotificationScope
import server.web.casa.utils.ApiResponse

@Tag(name = "Notification", description = "")
@RestController
@RequestMapping("api")
class NotificationRestController(
    private val service: NotificationSystemService,
) {
    @Operation(summary = "List notifications of user")
    @GetMapping("/{version}/${NotificationScope.PROTECTED}/owner/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllNotificationByUser(
        @PathVariable("userId") userId : Long,
    )= coroutineScope {
        val data = service.notificationByUser(userId)
        ApiResponse(data)
    }

    @Operation(summary = "Desactivate notification")
    @GetMapping("/{version}/${NotificationScope.PRIVATE}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun disableNotification(
        @PathVariable("id") id : Long,
    )= coroutineScope {
        service.notificationDisable(id)
        ResponseEntity.status(HttpStatus.CREATED).body(mapOf("message" to "cette notification a été supprimer avec succès"))
    }

//    @Operation(summary = "Desactivate notification")
//    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
//    suspend fun createNotification() = coroutineScope {
//
//    }
}