package server.web.casa.app.property.infrastructure.controller.favorite

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.property.application.service.TerrainService
import server.web.casa.app.property.application.service.favorite.FavoriteTerrainService
import server.web.casa.app.property.domain.model.favorite.*
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteTerrainEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.favorite.FavoriteTerrainScope
import server.web.casa.utils.*
import java.time.LocalDate

@Tag(name = "Favorite Terrain", description = "Gestion des favorites")
@RestController
@Profile(Mode.DEV)
@RequestMapping("api")
class FavoriteTerrainController(
    private val service: FavoriteTerrainService,
    private val userS: UserService,
    private val terS: TerrainService
) {
    @PostMapping("/{version}/${FavoriteTerrainScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFavorite(
        @Valid @RequestBody request: FavoriteTerrainRequest
    ): ResponseEntity<Map<String, Any?>> {
        val user = userS.findIdUser(request.userId)
        val terrain = terS.findById( request.terrainId )
        val favorite = FavoriteTerrainEntity(
            userId = user.userId!!,
            createdAt = LocalDate.now(),
            terrainId = terrain.id!!
        )
        val existingFavorite = service.getFavoriteIfExist(terrain.id!!, user.userId)
        val savedFavorite = existingFavorite.ifEmpty { service.create(favorite) }

        val response = mapOf(
            "data" to savedFavorite
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{version}/${FavoriteTerrainScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavorite(): ApiResponse<List<FavoriteTerrainDTO>> {
        val data = service.getAll()
        return ApiResponse(data)
    }

    @GetMapping("/{version}/${FavoriteTerrainScope.PROTECTED}/user/{user}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getUserFavoriteTerrain(@PathVariable user: Long):ResponseEntity<Map<String, List<FavoriteTerrainDTO>?>> {
        val user = userS.findIdUser(user)
        val favorite = service.getUserFavorite(user.userId!!)
        val response = mapOf("data" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/{version}/${FavoriteTerrainScope.PROTECTED}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteById(@PathVariable id: Long):ResponseEntity<Map<String, FavoriteTerrainDTO>> {
        val favorite = service.getById (id) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "favorite Not Found."
        )
        val response = mapOf("data" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/{version}/${FavoriteTerrainScope.PROTECTED}/salle/{terrainId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteProperty(@PathVariable terrainId: Long):ResponseEntity<Map<String, List<FavoriteTerrainDTO>?>> {
        val hotel = terS.findById( terrainId )
        val favorite = service.getFavoriteByTerrainId(terrainId)
        val response = mapOf("data" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @DeleteMapping("/{version}/${FavoriteTerrainScope.PROTECTED}/delete/{id}")
    suspend fun deleteFavorite(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        service.deleteById(id)
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }
    @DeleteMapping("/{version}/${FavoriteTerrainScope.PROTECTED}/delete/all")
    suspend fun deleteFavoriteAll(): ResponseEntity<Map<String, String>> {
        service.deleteAll()
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }
    @DeleteMapping("/{version}/${FavoriteTerrainScope.PROTECTED}/delete/{userId}/{id}")
    suspend fun deleteFavorite(@PathVariable userId: Long, @PathVariable id:Long): ResponseEntity<Map<String, String>> {
        val existingFavorite = service.getFavoriteIfExist(id, userId).firstOrNull()

        val deleteFavorite = existingFavorite?.favorite?.id?.let {
            service.deleteById(it)
        }
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{version}/${FavoriteTerrainScope.PROTECTED}/user/delete/{userId}")
    suspend fun deleteAllFavoriteByUser(@PathVariable userId: Long): ResponseEntity<Map<String, String>> {
        val user = userS.findIdUser(userId)
        service.deleteAllFavoriteUser(user.userId!!)
        val response = mapOf("message" to "Favorite deleted successfully")
        return ResponseEntity.ok(response)
    }
}