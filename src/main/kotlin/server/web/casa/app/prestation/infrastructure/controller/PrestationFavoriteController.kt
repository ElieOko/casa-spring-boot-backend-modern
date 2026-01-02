package server.web.casa.app.prestation.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.ecosystem.application.service.PrestationService
import server.web.casa.app.prestation.application.FavoritePrestationService
import server.web.casa.app.prestation.domain.model.FavoritePrestationDTO
import server.web.casa.app.prestation.domain.request.FavoritePrestationRequest
import server.web.casa.app.prestation.infrastructure.persistance.entity.FavoritePrestationEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.utils.Mode

const val ROUTE_FAVORITE_PRESTATION = "api/favorite/prestation"

@Tag(name = "Favorite Prestation", description = "Favorite Prestation's Management")
@RestController
@RequestMapping(ROUTE_FAVORITE_PRESTATION)
@Profile(Mode.DEV)

class PrestationFavoriteController(
    private val service: FavoritePrestationService,
    private val userS: UserService,
    private val presTS: PrestationService
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        @Valid @RequestBody request: FavoritePrestationRequest
    ): ResponseEntity<Map<String, Any?>>{
        val user = userS.findIdUser(request.userId)
            ?: return ResponseEntity.badRequest()
            .body(mapOf("error" to "User not found"))

        val prestation = presTS.getById(request.prestationId)
                ?: return ResponseEntity.badRequest()
                .body(mapOf("error" to "prestation not found"))

        val data = FavoritePrestationEntity(
            userId = user.userId,
            prestationId = prestation.id
        )
        val verify = service.findByPrestationIdAndUserId(prestation.id!!, user.userId!!)
        val created = verify ?: service.create(data)
        return ResponseEntity.status(201).body(mapOf("data" to created))
    }

    @GetMapping("/",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAll():ResponseEntity<Map<String,List<FavoritePrestationDTO>>>{
        val all = service.findAll()
        return ResponseEntity.ok().body(mapOf("data" to all))
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationById(@PathVariable id: Long): ResponseEntity<Map<String, FavoritePrestationDTO?>>{
        val f = service.findById(id)
        return ResponseEntity.ok().body(mapOf("data" to f))
    }

    @GetMapping("/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getByUser(@PathVariable userId: Long): ResponseEntity<Map<String, List<FavoritePrestationDTO>?>>{
        val f = service.findByUserId( userId)
        return ResponseEntity.ok(mapOf("data" to f))
    }
    @GetMapping("/{prestationId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getByPrestation(@PathVariable prestationId: Long): ResponseEntity<Map<String, List<FavoritePrestationDTO>?>>{
        val f = service.findByPrestationId(prestationId)
        return ResponseEntity.ok(mapOf("data" to f))
    }
    @GetMapping("/{userId}/{prestationId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getByUserPrestation(@PathVariable userId: Long, @PathVariable prestationId: Long): ResponseEntity<Map<String, List<FavoritePrestationDTO>?>>{
        val f = service.findByPrestationIdAndUserId(prestationId, userId)
       return ResponseEntity.ok(mapOf("data" to f))
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun deleteById(@PathVariable id: Long): ResponseEntity<Map<String, String>>{
        val f = service.findById(id)
        if (f != null) service.deleteById(f.favorite.id!!) else return ResponseEntity.badRequest().body(mapOf("error" to "favorite not found"))
        return ResponseEntity.ok().body(mapOf("message" to "Favorite deleted"))
    }

    @DeleteMapping("/delete/all")
    suspend fun deleteAll(): ResponseEntity<Map<String, String>>{
        service.deleteAll()
        return ResponseEntity.ok(mapOf("message" to "All favorite prestation deleted"))
    }
}
