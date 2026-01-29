package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.user.application.service.UserService
import server.web.casa.app.user.domain.model.request.UserRequestChange
import server.web.casa.security.Auth
import server.web.casa.utils.ApiResponse

@Tag(name = "Utilisateur", description = "Gestion des utilisateurs")
@RestController
@RequestMapping("api")
class UserController(
    val userService : UserService,
    val auth: Auth
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    @Operation(summary = "Liste des utilisateurs")
    @GetMapping("/{version}/protected/users")
    suspend fun getListUser() = coroutineScope {
        val session = auth.user()
        val state: Boolean? = session?.second?.find{ true }
        when (state) {
            true -> {
                val data = userService.findAllUser().toList()
                ApiResponse(data)
            }
            false,null -> ResponseEntity.status(403).body(mapOf("message" to "Accès non autorisé"))
        }
    }

    @Operation(summary = "Detail utilisateur")
    @GetMapping("/{version}/protected/users/{id}")
    suspend fun getUser(
        @PathVariable("id") id : Long
    ) = coroutineScope {
        val session = auth.user()
        val state: Boolean? = session?.second?.find{ true }
        when (state) {
            true -> {
                val data = userService.findIdUser(id)
                ResponseEntity.ok().body(data)
            }
            false,null -> ResponseEntity.status(403).body(mapOf("message" to "Accès non autorisé"))
        }
    }

    @Operation(summary = "Modification utilisateur")
    @PutMapping("/{version}/protected/users/{id}")
    suspend fun updateUser(
        @PathVariable("id") userId : Long,
        @RequestBody @Valid user : UserRequestChange
    ) = coroutineScope {
        val session = auth.user()
        val state: Boolean? = session?.second?.find{ true }
        if (session?.first?.userId == userId || state == true ) {
            val updated = userService.updateUser(userId,user)
            ResponseEntity.ok(updated)
        }
        ResponseEntity.status(403).body(mapOf("message" to "Accès non autorisé"))
    }
//
//    @Operation(summary = "Suppression utilisateur")
//    @DeleteMapping("/{version}/protected/users/{id}")
//    suspend fun delete(
//        @PathVariable("id") id : Long
//    ): ResponseEntity<Map<String, String>> {
//
//        userService.deleteUser(id)
//        val response = mapOf("message" to "Suppression réussi avec succès")
//        return ResponseEntity.ok().body(response)
//    }
}