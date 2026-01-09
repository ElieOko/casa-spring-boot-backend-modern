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
import server.web.casa.app.property.application.service.HotelService
import server.web.casa.app.property.application.service.favorite.FavoriteHotelService
import server.web.casa.app.property.domain.model.favorite.FavoriteHotelDTO
import server.web.casa.app.property.domain.model.favorite.FavoriteHotelRequest
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteHotelEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.favorite.FavoriteFestiveRoute
import server.web.casa.utils.ApiResponse
import server.web.casa.utils.Mode
import java.time.LocalDate

const val ROUTE_FAVORITE_HOTEL = FavoriteFestiveRoute.ROUTE_FAVORITE_HOTEL

@Tag(name = "Favorite Hotel", description = "Gestion des favorites")
@RestController
@Profile(Mode.DEV)
@RequestMapping(ROUTE_FAVORITE_HOTEL)
class FavoriteHotelController(
    private val service: FavoriteHotelService,
    private val userS: UserService,
    private val hotelS: HotelService
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFavorite(
        @Valid @RequestBody request: FavoriteHotelRequest
    ): ResponseEntity<Map<String, Any?>> {
        val user = userS.findIdUser(request.userId)
        val salle = hotelS.getAllHotel().find{ it.hotel.id == request.hotelId } ?: return ResponseEntity.badRequest().body(mapOf("error" to "hote not found"))
        val favorite = FavoriteHotelEntity(
            userId = user.userId!!,
            createdAt = LocalDate.now(),
            hotelId = salle.hotel.id!!
        )
        val existingFavorite = service.getFavoriteIfExist(salle.hotel.id, user.userId)
        val savedFavorite = existingFavorite.ifEmpty { service.create(favorite) }

        val response = mapOf(
            "data" to savedFavorite
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavorite(): ApiResponse<List<FavoriteHotelDTO>> {
        val data = service.getAll()
        return ApiResponse(data)
    }

    @GetMapping("/user/{user}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getUserFavoriteHotel(@PathVariable user: Long):ResponseEntity<Map<String, List<FavoriteHotelDTO>?>> {
        val user = userS.findIdUser(user)
        val favorite = service.getUserFavorite(user.userId!!)
        val response = mapOf("data" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/{hotelId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteById(@PathVariable hotelId: Long):ResponseEntity<Map<String, FavoriteHotelDTO>> {
        val favorite = service.getById(hotelId) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "favorite Not Found."
        )
        val response = mapOf("data" to favorite)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/salle/{hotelId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteProperty(@PathVariable hotelId: Long):ResponseEntity<Map<String, List<FavoriteHotelDTO>?>> {
        val hotel = hotelS.getAllHotel().find{ it.hotel.id == hotelId } ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "ID Is Not Found for User with ID ."
        )
        val favorite = service.getFavoriteByHotel(hotel.hotel.id!!)
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
    @DeleteMapping("/delete/{userId}/{hotelId}")
    suspend fun deleteFavorite(@PathVariable userId: Long, @PathVariable hotelId:Long): ResponseEntity<Map<String, String>> {
        val existingFavorite = service.getFavoriteIfExist(hotelId, userId).firstOrNull()

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