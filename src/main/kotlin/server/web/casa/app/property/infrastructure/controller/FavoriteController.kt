package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.property.application.service.FavoriteService
import server.web.casa.app.property.application.service.PropertyService
import server.web.casa.app.property.domain.model.Favorite
import server.web.casa.app.property.domain.model.request.FavoriteRequest
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyRepository
import server.web.casa.app.user.application.service.UserService
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import server.web.casa.route.favorite.FavoriteRoute
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

        if (user == null || property == null) {
            return ResponseEntity.badRequest().body(
                mapOf("error" to "User and property must not be null.")
            )
        }

        val favorite = Favorite(
            user = user,
            property = property,
            createdAt = LocalDate.now()
        )
       val existingFavorite = service.getFavoriteIfExist(propR.findById(request.propertyId).orElse(null), userR.findById(request.userId).orElse(null))?.firstOrNull()

        val savedFavorite = existingFavorite ?: service.create(favorite)
        val response = mapOf(
            "message" to "Property '${property.propertyId}' added to favorites successfully.",
            "user" to user,
            "favorite" to savedFavorite
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavorite(): ResponseEntity<Map<String, List<Favorite>>> {
        val data = service.getAll()
        val response = mapOf("favorites" to data)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/user/{user}", produces = [MediaType.APPLICATION_JSON_VALUE])
     fun getUserFavoriteProperty(@PathVariable user: Long):ResponseEntity<Map<String, List<Favorite>?>> {
        val user = userR.findById(user).orElse(null)
        val favorite = if(user != null) service.getUserFavoriteProperty(user) else null
        val response = mapOf("favorites" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/property/{property}", produces = [MediaType.APPLICATION_JSON_VALUE])
     fun getOneFavoriteProperty(@PathVariable property: Long):ResponseEntity<Map<String, List<Favorite>?>> {
        val property = propR.findById (property).orElse(null)
        val favorite = service.getOneFavoritePropertyCount(property)
        val response = mapOf("favorites" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @DeleteMapping("/delete/{id}")
     fun deleteFavorite(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        service.deleteById(id)
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/user/delete/{user}")
     fun deleteAllFavoriteByUser(@PathVariable user: Long): ResponseEntity<Map<String, String>> {
        val user = userR.findById(user).orElse(null)
        service.deleteAllFavoriteUser(user)
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }
}