package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import server.web.casa.app.user.application.UserService
import server.web.casa.app.user.domain.model.User
import server.web.casa.app.user.domain.model.request.UserRequestChange
import server.web.casa.security.Auth

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
    @Transactional
//    @PreAuthorize("hasRole('ADMIN')")
    suspend fun getListUser(): ResponseEntity<List<User?>>{
//        val ownerId = SecurityContextHolder.getContext().authentication!!.principal as String
        val data = userService.findAllUser()
        return ResponseEntity.ok().body(data)
    }

    @Operation(summary = "Detail utilisateur")
    @GetMapping("/{id}")
    @Transactional
//    @PreAuthorize("hasRole('ADMIN')")
    suspend fun getUser(
        @PathVariable("id") id : Long
    ) : ResponseEntity<User> {
        val ownerId = SecurityContextHolder.getContext().authentication!!.principal as String
        val data = userService.findIdUser(id)
        return ResponseEntity.ok().body(data)
    }

    @Operation(summary = "Modification utilisateur")
    @PutMapping("/{id}")
    @Transactional
    suspend fun updateUser(
        @RequestBody @Valid user : UserRequestChange
    ) : ResponseEntity<User> {
        val ownerId = SecurityContextHolder.getContext().authentication!!.principal as String
        val id = auth.user()?.userId
        val updated = userService.updateUser(id!!,user)
        return ResponseEntity.ok(updated)
    }

    @Operation(summary = "Suppression utilisateur")
    @DeleteMapping("/{id}")
    @Transactional
//    @PreAuthorize("hasRole('ADMIN')")
    suspend fun delete(
        @PathVariable("id") id : Long
    ): ResponseEntity<Map<String, String>> {
        val ownerId = SecurityContextHolder.getContext().authentication!!.principal as String
        userService.deleteUser(id)
        val response = mapOf("message" to "Suppression réussi avec succès")
        return ResponseEntity.ok().body(response)
    }
}