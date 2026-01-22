package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.user.application.service.UserService
import server.web.casa.app.user.domain.model.UserDto
import server.web.casa.app.user.domain.model.request.UserRequestChange
import server.web.casa.security.Auth
import server.web.casa.utils.ApiResponse

@Tag(name = "Utilisateur", description = "Gestion des utilisateurs")
@RestController
@RequestMapping("api/users")
class UserController(
    val userService : UserService,
    val auth: Auth
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    @Operation(summary = "Liste des utilisateurs")
    @GetMapping
//    @Transactional
//    @PreAuthorize("hasRole('ADMIN')")
    suspend fun getListUser(): ApiResponse<List<UserDto>> {
//        val ownerId = SecurityContextHolder.getContext().authentication!!.principal as String
//        logger.info("My ID -> $ownerId")
        val data = userService.findAllUser().toList()
        return ApiResponse(data)
    }

    @Operation(summary = "Detail utilisateur")
    @GetMapping("/{id}")
//    @Transactional
//    @PreAuthorize("hasRole('ADMIN')")
    suspend fun getUser(
        @PathVariable("id") id : Long
    ) = coroutineScope {
        val data = userService.findIdUser(id)
        ResponseEntity.ok().body(data)
    }

    @Operation(summary = "Modification utilisateur")
    @PutMapping("/{id}")
//    @Transactional
    suspend fun updateUser(
        @RequestBody @Valid user : UserRequestChange
    ) : ResponseEntity<UserDto> {
//        val ownerId = SecurityContextHolder.getContext().authentication!!.principal as String
        val id = auth.user()?.first?.userId
        val updated = userService.updateUser(id!!,user)
        return ResponseEntity.ok(updated)
    }

    @Operation(summary = "Suppression utilisateur")
    @DeleteMapping("/{id}")
//    @Transactional
//    @PreAuthorize("hasRole('ADMIN')")
    suspend fun delete(
        @PathVariable("id") id : Long
    ): ResponseEntity<Map<String, String>> {
//        val ownerId = SecurityContextHolder.getContext().authentication!!.principal as String
        userService.deleteUser(id)
        val response = mapOf("message" to "Suppression réussi avec succès")
        return ResponseEntity.ok().body(response)
    }
}