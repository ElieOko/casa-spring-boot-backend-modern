package server.web.casa.app.property.infrastructure.controller.favorite

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.property.application.service.SalleFestiveService
import server.web.casa.app.property.application.service.favorite.FavoriteFestiveService
import server.web.casa.app.property.domain.model.favorite.FavoriteFestiveDTO
import server.web.casa.app.property.domain.model.favorite.FavoriteFestiveRequest
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteFestiveEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.favorite.FavoriteFestiveRoute
import server.web.casa.utils.ApiResponse
import server.web.casa.utils.Mode
import java.time.LocalDate

const val ROUTE_FAVORITE = FavoriteFestiveRoute.FAVORITE_PATH

@Tag(name = "Favorite Salle Festive", description = "Gestion des favorites")
@RestController
@Profile(Mode.DEV)
@RequestMapping(ROUTE_FAVORITE)
class FavoriteFestiveController(
    private val service: FavoriteFestiveService,
    private val userS: UserService,
    private val salleS: SalleFestiveService
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFavorite(
        @Valid @RequestBody request: FavoriteFestiveRequest
    ): ResponseEntity<Map<String, Any?>> {
        val user = userS.findIdUser(request.userId)
        val salle = salleS.findById(request.festId)

        val favorite = FavoriteFestiveEntity(
            userId = user.userId!!,
            createdAt = LocalDate.now(),
            festiveId = salle.id!!
        )
        val existingFavorite = service.getFavoriteIfExist(salle.id!!, user.userId!!)
        val savedFavorite = if (!existingFavorite.isNullOrEmpty()) existingFavorite else service.create(favorite)

        val response = mapOf(
            "data" to savedFavorite
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavorite(): ApiResponse<List<FavoriteFestiveDTO>> {
        val data = service.getAll()
        return ApiResponse(data)
    }

    @GetMapping("/user/{user}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getUserFavoriteProperty(@PathVariable user: Long):ResponseEntity<Map<String, List<FavoriteFestiveDTO>?>> {
        val user = userS.findIdUser(user)
        val favorite = service.getUserFavorite(user.userId!!)
        val response = mapOf("data" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/{festId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteById(@PathVariable favoriteId: Long):ResponseEntity<Map<String, FavoriteFestiveDTO>> {
        val favorite = service.getById(favoriteId) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "favorite Not Found."
        )
        val response = mapOf("favorites" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/salle/{festId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteProperty(@PathVariable festId: Long):ResponseEntity<Map<String, List<FavoriteFestiveDTO>?>> {
        val salle = salleS.findById (festId)
        val favorite = service.getFavoriteByFestId(salle.id!!)
        val response = mapOf("data" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @DeleteMapping("/delete/{id}")
    suspend fun deleteFavorite(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        service.deleteById(id)
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }
    @DeleteMapping("/delete/all")
    suspend fun deleteFavoriteAll(): ResponseEntity<Map<String, String>> {
        service.deleteAll()
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }
    @DeleteMapping("/delete/{userId}/{festId}")
    suspend fun deleteFavorite(@PathVariable userId: Long, @PathVariable propertyId:Long): ResponseEntity<Map<String, String>> {
        val existingFavorite = service.getFavoriteIfExist(propertyId, userId).firstOrNull()

        val deleteFavorite = existingFavorite?.favorite?.id?.let {
            service.deleteById(it)
        }
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/user/delete/{userId}")
    suspend fun deleteAllFavoriteByUser(@PathVariable userId: Long): ResponseEntity<Map<String, String>> {
        val user = userS.findIdUser(userId)
        service.deleteAllFavoriteUser(user.userId!!)
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }
}