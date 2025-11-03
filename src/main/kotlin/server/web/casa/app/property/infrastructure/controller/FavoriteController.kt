package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.property.application.service.FavoriteService
import server.web.casa.app.property.application.service.FeatureService
import server.web.casa.app.property.application.service.PropertyService
import server.web.casa.app.property.domain.model.Favorite
import server.web.casa.app.property.domain.model.request.FavoriteRequest
import server.web.casa.app.property.infrastructure.persistence.repository.FeatureRepository
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyRepository
import server.web.casa.app.user.application.UserService
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import server.web.casa.route.favorite.FavoriteRoute
import java.time.LocalDate

const val ROUTE_FAVORITE = FavoriteRoute.FAVORITE_PATH

@Tag(name = "Favorite", description = "Gestion des favorites")
@RestController
@RequestMapping(ROUTE_FAVORITE)
class FavoriteController(
    private val service: FavoriteService,
    private val featureS: FeatureService,
    private val featureR: FeatureRepository,
    private val userS: UserService,
    private val userR: UserRepository,
    private val prop: PropertyService,
    private val propR: PropertyRepository
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFeature(@Valid @RequestBody request: FavoriteRequest): ResponseEntity<Map<String, Any?>>{
        val user = userS.findIdUser(request.userId)
        val property = prop.findByIdProperty(request.propertyId)
        val feature = featureS.findByIdFeature(request.featureId)
        val data = Favorite(
            user = user,
            property = property,
            feature = feature,
            createdAt = LocalDate.now()
        )

        if ((property == null) == (feature == null)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf("error" to "You must assign either property or feature, not both or none."))
        }

        val favoriteCreate = service.create(data)
        val message = if (property == null) {
            "Property ${favoriteCreate.property} add to favorite with success"
        } else {
            "Feature ${favoriteCreate.feature} add to favorite with success"
        }

        val response = mapOf(
            "message" to message,
            "user" to user,
            "favorite" to favoriteCreate
        )
        return ResponseEntity.status(201).body(response)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavorite(): ResponseEntity<Map<String, List<Favorite>>> {
        val data = service.getAll()
        val response = mapOf("favorites" to data)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/user/property/{user}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getUserFavoriteProperty(@PathVariable user: Long):ResponseEntity<Map<String, List<Favorite>?>> {
        val user = userR.findById(user).orElse(null)
        val favorite = service.getUserFavoriteProperty(user)
        val response = mapOf("favorites" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/user/feature/{user}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getUserFavoriteFeature(@PathVariable user: Long):ResponseEntity<Map<String, List<Favorite>?>> {
        val user = userR.findById(user).orElse(null)
        val favorite = service.getUserFavoriteFeature(user)
        val response = mapOf("favorites" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/feature/{feature}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteFeature(@PathVariable feature: Long):ResponseEntity<Map<String, List<Favorite>?>> {
        val feature = featureR.findById (feature).orElse(null)
        val favorite = service.getOneFavoriteFeatureCount(feature)
        val response = mapOf("favorites" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/property/{property}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteProperty(@PathVariable property: Long):ResponseEntity<Map<String, List<Favorite>?>> {
        val property = propR.findById (property).orElse(null)
        val favorite = service.getOneFavoritePropertyCount(property)
        val response = mapOf("favorites" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/property", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavoriteProperty():ResponseEntity<Map<String, List<Favorite>?>> {
        val favorite = service.getAllFavoriteProperty()
        val response = mapOf("favorites" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/feature", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavoriteFeature():ResponseEntity<Map<String, List<Favorite>?>> {
        val favorite = service.getAllFavoriteFeature()
        val response = mapOf("favorites" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @DeleteMapping("/delete/{id}")
    suspend fun deleteFavorite(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        service.deleteById(id)
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/user/delete/feature/{user}")
    suspend fun deleteAllFavoriteFeatureByUser(@PathVariable user: Long): ResponseEntity<Map<String, String>> {
        val user = userR.findById(user).orElse(null)
        service.deleteAllFavoriteFeatureUser(user)
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/user/delete/property/{user}")
    suspend fun deleteAllFavoritePropertyByUser(@PathVariable user: Long): ResponseEntity<Map<String, String>> {
        val user = userR.findById(user).orElse(null)
        service.deleteAllFavoritePropertyUser(user)
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/user/delete/{user}")
    suspend fun deleteAllFavoriteByUser(@PathVariable user: Long): ResponseEntity<Map<String, String>> {
        val user = userR.findById(user).orElse(null)
        service.deleteAllFavoriteUser(user)
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }
}