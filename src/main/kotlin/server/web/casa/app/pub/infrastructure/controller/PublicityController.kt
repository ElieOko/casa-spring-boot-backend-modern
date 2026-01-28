package server.web.casa.app.pub.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.pub.application.PublicityService
import server.web.casa.app.pub.domain.model.PublicityRequest
import server.web.casa.app.pub.infrastructure.persistance.entity.PublicityEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.pub.PubScope
import server.web.casa.utils.Mode
import java.time.LocalDate

@Tag(name = "Publicity", description = "Publicity's Management")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)
class PublicityController(
    private val service: PublicityService,
    private val userS: UserService
) {
    @PostMapping("/{version}/${PubScope.PROTECTED}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(@Valid @RequestBody request: PublicityRequest
    ): ResponseEntity<Map<String, Any?>> {
            val user = userS.findIdUser(request.userId)
                    ?: return ResponseEntity.badRequest()
                    .body(mapOf("error" to "User not found"))

            val pub = PublicityEntity(
                user = user.userId,
                imagePath = request.imagePath,
                title = request.title,
                description = request.description,
                isActive = true,
                createdAt = LocalDate.now()
            )
            val pubCreate = service.createPub(pub)
            val response = mapOf(
                "message" to "pub create with success",
                "pub" to pubCreate,
                "user" to user
            )
            return ResponseEntity.status(201).body(response)
        }

    @GetMapping("/",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPub(): ResponseEntity<Map<String, List<PublicityEntity>>> {
            val pub = service.findAllPub()
            val response = mapOf("pub" to pub)
            return ResponseEntity.ok().body(response)
        }

    @GetMapping("/{version}/${PubScope.PUBLIC}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getPubById(@PathVariable id: Long): ResponseEntity<Map<String, PublicityEntity?>> {
        val pub = service.findId(id)
        val response = mapOf("pub" to pub)
        return ResponseEntity.ok(response)
    }
    @GetMapping("/{version}/${PubScope.PROTECTED}/user/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getPubByUser(@PathVariable id: Long): ResponseEntity<Map<String, List<PublicityEntity?>>> {
        val user = userS.findIdUser(id) ?: throw RuntimeException("User not found")
        val pub = service.findByUser(user.userId!!)
        val response = mapOf("pub" to pub)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{version}/${PubScope.PROTECTED}/date/{createdAt}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getPubByDate(@PathVariable createdAt: LocalDate): ResponseEntity<Map<String, List<PublicityEntity?>>> {
        val pub = service.findByCreated(createdAt)
        val response = mapOf("pub" to pub)
        return ResponseEntity.ok(response)
    }
    @PutMapping("/{version}/${PubScope.PROTECTED}/update/active/{id}")
    suspend fun updateReservation(
        @PathVariable id: Long,
        @RequestBody state: Boolean
    ): ResponseEntity<Map<String, PublicityEntity?>> {
        val pub = service.findId(id)
            ?: throw RuntimeException("Publicity not found")
        val updated = service.updateIsActive(id, state )
        val pubUpdate = service.findId(id)
        return ResponseEntity.ok(mapOf("pub" to pubUpdate))
    }

    @DeleteMapping("/{version}/${PubScope.PROTECTED}/delete/{id}")
    suspend fun deletePub(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        val pub = service.findId(id)
            ?: throw RuntimeException("Publicity not found")
        val deleted = service.deleteById(id)
        return ResponseEntity.ok(mapOf("message" to "publicity deleted"))
    }
}