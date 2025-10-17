package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.user.application.UserService
import server.web.casa.app.user.domain.model.User

@Tag(name = "Utilisateur", description = "Gestion des utilisateurs")
@RestController
@RequestMapping("users")
class UserController(
    val userService : UserService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Operation(summary = "Liste des utilisateurs")
    @GetMapping
    fun getListUser(): ResponseEntity<List<User?>>{
        val data = userService.findAllUser()
        return ResponseEntity.ok().body(data)
    }

    @Operation(summary = "Detail utilisateur")
    @GetMapping("/{id}")
    fun getUser(
        @PathVariable("id") id : Long
    ) : ResponseEntity<User> {
        val data = userService.findIdUser(id)
        return ResponseEntity.ok().body(data)
    }

    @Operation(summary = "Modification utilisateur")
    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable("id") id : Long,
        @RequestBody @Valid user : User
    ) : ResponseEntity<User> {
        val updated = userService.updateUser(id,user)
        return ResponseEntity.ok(updated)
    }

    @Operation(summary = "Suppression utilisateur")
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable("id") id : Long
    ): ResponseEntity<Map<String, String>> {
        userService.deleteUser(id)
        val response = mapOf("message" to "Suppression réussi avec succès")
        return ResponseEntity.ok().body(response)
    }
}