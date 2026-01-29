package server.web.casa.app.prestation.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.ecosystem.application.service.PrestationService
import server.web.casa.app.prestation.application.FavoritePrestationService
import server.web.casa.app.prestation.domain.model.FavoritePrestationDTO
import server.web.casa.app.prestation.domain.request.FavoritePrestationRequest
import server.web.casa.app.prestation.infrastructure.persistance.entity.FavoritePrestationEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.favorite.PrestationFavoriteScope
import server.web.casa.utils.Mode

@Tag(name = "Favorite Prestation", description = "Favorite Prestation's Management")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)

class PrestationFavoriteController(
    private val service: FavoritePrestationService,
    private val userS: UserService,
    private val presTS: PrestationService
) {

    @PostMapping("/{version}/${PrestationFavoriteScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createPrestationFavorite(
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
        //val created = verify ?: service.create(data)
        val created = verify?.takeIf { it.isNotEmpty() } ?: service.create(data)
        return ResponseEntity.status(201).body(mapOf("data" to created))
    }

    @GetMapping("/{version}/${PrestationFavoriteScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavortitePrestation():ResponseEntity<Map<String,List<FavoritePrestationDTO>>>{
        val all = service.findAll()
        return ResponseEntity.ok().body(mapOf("data" to all))
    }

    @GetMapping("/{version}/${PrestationFavoriteScope.PROTECTED}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationPrestationById(@PathVariable id: Long): ResponseEntity<Map<String, FavoritePrestationDTO?>>{
        val find = service.findById(id)
        return ResponseEntity.ok().body(mapOf("data" to find))
    }

    @GetMapping("/{version}/${PrestationFavoriteScope.PROTECTED}/user/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getByUserPrestation(@PathVariable userId: Long): ResponseEntity<Map<String, List<FavoritePrestationDTO>?>>{
        val find = service.findByUserId( userId)
        return ResponseEntity.ok(mapOf("data" to find))
    }
    @GetMapping("/{version}/${PrestationFavoriteScope.PROTECTED}/prestation/{prestationId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getByPrestationFavorite(@PathVariable prestationId: Long): ResponseEntity<Map<String, List<FavoritePrestationDTO>?>>{
        val find = service.findByPrestationId(prestationId)
        return ResponseEntity.ok(mapOf("data" to find))
    }
    @GetMapping("/{version}/${PrestationFavoriteScope.PROTECTED}/{userId}/{prestationId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getByUserPrestation(@PathVariable userId: Long, @PathVariable prestationId: Long): ResponseEntity<Map<String, List<FavoritePrestationDTO>?>>{
        val find = service.findByPrestationIdAndUserId(prestationId, userId)
       return ResponseEntity.ok(mapOf("data" to find))
    }

    @DeleteMapping("/{version}/${PrestationFavoriteScope.PROTECTED}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun deleteByIdFavoritePrestation(@PathVariable id: Long): ResponseEntity<Map<String, String>>{
        val find = service.findById(id)
        if (find != null) service.deleteById(find.favorite.id!!) else return ResponseEntity.badRequest().body(mapOf("error" to "favorite not found"))
        return ResponseEntity.ok().body(mapOf("message" to "Favorite deleted"))
    }

    @DeleteMapping("/{version}/${PrestationFavoriteScope.PROTECTED}/delete/all")
    suspend fun deleteAllFavoritePrestation(): ResponseEntity<Map<String, String>>{
        service.deleteAll()
        return ResponseEntity.ok(mapOf("message" to "All favorite prestation deleted"))
    }
}
