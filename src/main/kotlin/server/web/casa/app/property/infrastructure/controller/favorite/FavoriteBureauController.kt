package server.web.casa.app.property.infrastructure.controller.favorite

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.property.application.service.BureauService
import server.web.casa.app.property.application.service.favorite.FavoriteBureauService
import server.web.casa.app.property.domain.model.favorite.*
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteBureauEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.favorite.FavoriteBureauScope
import server.web.casa.utils.*
import java.time.LocalDate

@Tag(name = "Favorite BUREAU", description = "Gestion des favorites")
@RestController
@Profile(Mode.DEV)
@RequestMapping("api")
class FavoriteBureauController(
    private val service: FavoriteBureauService,
    private val userS: UserService,
    private val brxS: BureauService
) {
    @PostMapping("/{version}/${FavoriteBureauScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFavorite(
        @Valid @RequestBody request: FavoriteBureauRequest
    ): ResponseEntity<Map<String, Any?>> {
        val user = userS.findIdUser(request.userId)
        val salle = brxS.findById(request.bureauId)

        val favorite = FavoriteBureauEntity(
            userId = user.userId!!,
            createdAt = LocalDate.now(),
            bureauId = salle.id!!
        )
        val existingFavorite = service.getFavoriteIfExist(salle.id!!, user.userId!!)
        val savedFavorite = if (!existingFavorite.isNullOrEmpty()) existingFavorite else service.create(favorite)

        val response = mapOf(
            "data" to savedFavorite
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{version}/${FavoriteBureauScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavorite(): ApiResponse<List<FavoriteBureauDTO>> {
        val data = service.getAll()
        return ApiResponse(data)
    }

    @GetMapping("/{version}/${FavoriteBureauScope.PROTECTED}/user/{user}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getUserFavoriteProperty(@PathVariable user: Long):ResponseEntity<Map<String, List<FavoriteBureauDTO>?>> {
        val user = userS.findIdUser(user)
        val favorite = service.getUserFavorite(user.userId!!)
        val response = mapOf("data" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/{version}/${FavoriteBureauScope.PROTECTED}/{festId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteById(@PathVariable festId: Long):ResponseEntity<Map<String, FavoriteBureauDTO>> {
        val favorite = service.getById(festId) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "favorite Not Found."
        )
        val response = mapOf("favorites" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/{version}/${FavoriteBureauScope.PROTECTED}/salle/{festId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getFavoriteOneFestive(@PathVariable festId: Long):ResponseEntity<Map<String, List<FavoriteBureauDTO>?>> {
        val salle = brxS.findById (festId)
        val favorite = service.getFavoriteByFestId(salle.id!!)
        val response = mapOf("data" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @DeleteMapping("/{version}/${FavoriteBureauScope.PROTECTED}/delete/{id}")
    suspend fun deleteFavorite(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        service.deleteById(id)
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }
    @DeleteMapping("/{version}/${FavoriteBureauScope.PROTECTED}/delete/all")
    suspend fun deleteFavoriteAll(): ResponseEntity<Map<String, String>> {
        service.deleteAll()
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }
    @DeleteMapping("/{version}/${FavoriteBureauScope.PROTECTED}/delete/{userId}/{festId}")
    suspend fun deleteFavorite(@PathVariable userId: Long, @PathVariable festId:Long): ResponseEntity<Map<String, String>> {
        val existingFavorite = service.getFavoriteIfExist(festId, userId).firstOrNull()

        val deleteFavorite = existingFavorite?.favorite?.id?.let {
            service.deleteById(it)
        }
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{version}/${FavoriteBureauScope.PROTECTED}/user/delete/{userId}")
    suspend fun deleteAllFavoriteByUser(@PathVariable userId: Long): ResponseEntity<Map<String, String>> {
        val user = userS.findIdUser(userId)
        service.deleteAllFavoriteUser(user.userId!!)
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }
}