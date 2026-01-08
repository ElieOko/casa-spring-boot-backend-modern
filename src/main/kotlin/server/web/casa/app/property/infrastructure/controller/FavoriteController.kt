package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.flow.Flow
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.property.application.service.FavoriteService
import server.web.casa.app.property.application.service.PropertyService
import server.web.casa.app.property.domain.model.Favorite
import server.web.casa.app.property.domain.model.FavoriteDTO
import server.web.casa.app.property.domain.model.request.FavoriteRequest
import server.web.casa.app.property.infrastructure.persistence.entity.FavoriteEntity
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyRepository
import server.web.casa.app.user.application.service.UserService
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import server.web.casa.route.favorite.FavoriteRoute
import server.web.casa.utils.ApiResponse
import server.web.casa.utils.Mode
import java.time.LocalDate

const val ROUTE_FAVORITE = FavoriteRoute.FAVORITE_PATH

@Tag(name = "Favorite", description = "Gestion des favorites")
@RestController
@Profile(Mode.DEV)
@RequestMapping(ROUTE_FAVORITE)

class FavoriteController(
    private val service: FavoriteService,
    private val userS: UserService,
    private val userR: UserRepository,
    private val prop: PropertyService,
    private val propR: PropertyRepository
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFavorite(
        @Valid @RequestBody request: FavoriteRequest
    ): ResponseEntity<Map<String, Any?>> {
        val user = userS.findIdUser(request.userId)
        val property = prop.findByIdProperty(request.propertyId)

        val favorite = FavoriteEntity(
            userId = user.userId,
            propertyId = property.first.property.propertyId,
            createdAt = LocalDate.now()
        )
       val existingFavorite = service.getFavoriteIfExist(property.first.property.propertyId!!, user.userId!!)
       val savedFavorite = if (!existingFavorite.isNullOrEmpty()) existingFavorite else service.create(favorite)

        val response = mapOf(
            "message" to "Property '${property.first.property.propertyId}' added to favorites successfully.",
            "user" to user,
            "favorite" to savedFavorite
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavorite(): ApiResponse<List<FavoriteDTO>> {
        val data = service.getAll()
        return ApiResponse(data)
    }

    @GetMapping("/user/{user}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getUserFavoriteProperty(@PathVariable user: Long):ResponseEntity<Map<String, List<FavoriteDTO>?>> {
        val user = userR.findById(user) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "ID Is Not Found."
        )
        val favorite = service.getUserFavoriteProperty(user.userId!!)
        val response = mapOf("favorites" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/{favoriteId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteById(@PathVariable favoriteId: Long):ResponseEntity<Map<String, FavoriteDTO>> {
        val favorite = service.getById(favoriteId) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "favorite Not Found."
        )
        val response = mapOf("favorites" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/property/{property}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteProperty(@PathVariable property: Long):ResponseEntity<Map<String, List<FavoriteDTO>?>> {
        val property = propR.findById (property) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "ID Is Not Found."
        )
        val favorite = service.getOneFavoritePropertyCount(property.id!!)
        val response = mapOf("favorites" to favorite)
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
    @DeleteMapping("/delete/{userId}/{propertyId}")
    suspend fun deleteFavorite(@PathVariable userId: Long, @PathVariable propertyId:Long): ResponseEntity<Map<String, String>> {
        val existingFavorite = service.getFavoriteIfExist(propertyId, userId)?.firstOrNull()

        val deleteFavorite = existingFavorite?.favorite?.id?.let {
            service.deleteById(it)
        }
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/user/delete/{userId}")
    suspend fun deleteAllFavoriteByUser(@PathVariable userId: Long): ResponseEntity<Map<String, String>> {
        val user = userR.findById(userId) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "User not found."
        )
        service.deleteAllFavoriteUser(user.userId!!)
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }
}