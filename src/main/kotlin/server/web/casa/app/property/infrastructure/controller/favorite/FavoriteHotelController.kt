package server.web.casa.app.property.infrastructure.controller.favorite

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.property.application.service.HotelService
import server.web.casa.app.property.application.service.favorite.FavoriteHotelService
import server.web.casa.app.property.domain.model.favorite.*
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteHotelEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.favorite.FavoriteHotelScope
import server.web.casa.utils.*
import java.time.LocalDate
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Favorite Hotel", description = "Gestion des favorites")
@RestController
@Profile(Mode.DEV)
@RequestMapping("api")
class FavoriteHotelController(
    private val service: FavoriteHotelService,
    private val userS: UserService,
    private val hotelS: HotelService,
    private val sentry: SentryService,
) {
    @PostMapping("/{version}/${FavoriteHotelScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFavorite(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: FavoriteHotelRequest
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
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
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.favoritehotel.createfavorite.count",
                    distributionName = "api.favoritehotel.createfavorite.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteHotelScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavorite(request: HttpServletRequest): ApiResponse<List<FavoriteHotelDTO>> {
        val startNanos = System.nanoTime()
        try {
            val data = service.getAll()
            return ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritehotel.getallfavorite.count",
                    distributionName = "api.favoritehotel.getallfavorite.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteHotelScope.PROTECTED}/user/{user}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getUserFavoriteHotel(request: HttpServletRequest, @PathVariable user: Long):ResponseEntity<Map<String, List<FavoriteHotelDTO>?>> {
        val startNanos = System.nanoTime()
        try {
            val user = userS.findIdUser(user)
            val favorite = service.getUserFavorite(user.userId!!)
            val response = mapOf("data" to favorite)
            return ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritehotel.getuserfavoritehotel.count",
                    distributionName = "api.favoritehotel.getuserfavoritehotel.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteHotelScope.PROTECTED}/{hotelId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteById(request: HttpServletRequest, @PathVariable hotelId: Long):ResponseEntity<Map<String, FavoriteHotelDTO>> {
        val startNanos = System.nanoTime()
        try {
            val favorite = service.getById(hotelId) ?: throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "favorite Not Found."
            )
            val response = mapOf("data" to favorite)
            return ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritehotel.getonefavoritebyid.count",
                    distributionName = "api.favoritehotel.getonefavoritebyid.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteHotelScope.PROTECTED}/salle/{hotelId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteProperty(request: HttpServletRequest, @PathVariable hotelId: Long):ResponseEntity<Map<String, List<FavoriteHotelDTO>?>> {
        val startNanos = System.nanoTime()
        try {
            val hotel = hotelS.getAllHotel().find{ it.hotel.id == hotelId } ?: throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "ID Is Not Found for User with ID ."
            )
            val favorite = service.getFavoriteByHotel(hotel.hotel.id!!)
            val response = mapOf("data" to favorite)
            return ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritehotel.getonefavoriteproperty.count",
                    distributionName = "api.favoritehotel.getonefavoriteproperty.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${FavoriteHotelScope.PROTECTED}/delete/{id}")
    suspend fun deleteFavorite(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        try {
            service.deleteById(id)
            val response = mapOf("message" to "Favorite deleted successfully")
            return ResponseEntity.ok(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritehotel.deletefavorite.count",
                    distributionName = "api.favoritehotel.deletefavorite.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${FavoriteHotelScope.PROTECTED}/delete/all")
    suspend fun deleteFavoriteAll(request: HttpServletRequest): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        try {
            service.deleteAll()
            val response = mapOf("message" to "Favorite deleted successfully")
            return ResponseEntity.ok(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritehotel.deletefavoriteall.count",
                    distributionName = "api.favoritehotel.deletefavoriteall.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${FavoriteHotelScope.PROTECTED}/delete/{userId}/{hotelId}")
    suspend fun deleteFavorite(request: HttpServletRequest, @PathVariable userId: Long, @PathVariable hotelId:Long): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        try {
            val existingFavorite = service.getFavoriteIfExist(hotelId, userId).firstOrNull()

            val deleteFavorite = existingFavorite?.favorite?.id?.let {
                service.deleteById(it)
            }
            val response = mapOf("message" to "Favorite deleted successfully")
            return ResponseEntity.ok(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritehotel.deletefavorite.count",
                    distributionName = "api.favoritehotel.deletefavorite.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${FavoriteHotelScope.PROTECTED}/user/delete/{userId}")
    suspend fun deleteAllFavoriteByUser(request: HttpServletRequest, @PathVariable userId: Long): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        try {
            val user = userS.findIdUser(userId)
            service.deleteAllFavoriteUser(user.userId!!)
            val response = mapOf("message" to "Favorite deleted successfully")
            return ResponseEntity.ok(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritehotel.deleteallfavoritebyuser.count",
                    distributionName = "api.favoritehotel.deleteallfavoritebyuser.latency"
                )
            )
        }
    }
}