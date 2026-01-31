package server.web.casa.app.property.infrastructure.controller.favorite

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.property.application.service.SalleFestiveService
import server.web.casa.app.property.application.service.favorite.FavoriteFestiveService
import server.web.casa.app.property.domain.model.favorite.*
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteFestiveEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.favorite.FavoriteScope
import server.web.casa.utils.*
import java.time.*
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Favorite Salle Festive", description = "Gestion des favorites")
@RestController
@Profile(Mode.DEV)
@RequestMapping("api")
class FavoriteFestiveController(
    private val service: FavoriteFestiveService,
    private val userS: UserService,
    private val salleS: SalleFestiveService,
    private val sentry: SentryService,
) {
    @PostMapping("/{version}/${FavoriteScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFavorite(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: FavoriteFestiveRequest
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
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
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.favoritefestive.createfavorite.count",
                    distributionName = "api.favoritefestive.createfavorite.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavorite(request: HttpServletRequest): ApiResponse<List<FavoriteFestiveDTO>> {
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
                    countName = "api.favoritefestive.getallfavorite.count",
                    distributionName = "api.favoritefestive.getallfavorite.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteScope.PROTECTED}/user/{user}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getUserFavoriteProperty(request: HttpServletRequest, @PathVariable user: Long):ResponseEntity<Map<String, List<FavoriteFestiveDTO>?>> {
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
                    countName = "api.favoritefestive.getuserfavoriteproperty.count",
                    distributionName = "api.favoritefestive.getuserfavoriteproperty.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteScope.PROTECTED}/{festId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteById(request: HttpServletRequest, @PathVariable festId: Long):ResponseEntity<Map<String, FavoriteFestiveDTO>> {
        val startNanos = System.nanoTime()
        try {
            val favorite = service.getById(festId) ?: throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "favorite Not Found."
            )
            val response = mapOf("favorites" to favorite)
            return ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritefestive.getonefavoritebyid.count",
                    distributionName = "api.favoritefestive.getonefavoritebyid.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteScope.PROTECTED}/salle/{festId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getFavoriteOneFestive(request: HttpServletRequest, @PathVariable festId: Long):ResponseEntity<Map<String, List<FavoriteFestiveDTO>?>> {
        val startNanos = System.nanoTime()
        try {
            val salle = salleS.findById (festId)
            val favorite = service.getFavoriteByFestId(salle.id!!)
            val response = mapOf("data" to favorite)
            return ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritefestive.getfavoriteonefestive.count",
                    distributionName = "api.favoritefestive.getfavoriteonefestive.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${FavoriteScope.PROTECTED}/delete/{id}")
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
                    countName = "api.favoritefestive.deletefavorite.count",
                    distributionName = "api.favoritefestive.deletefavorite.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${FavoriteScope.PROTECTED}/delete/all")
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
                    countName = "api.favoritefestive.deletefavoriteall.count",
                    distributionName = "api.favoritefestive.deletefavoriteall.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${FavoriteScope.PROTECTED}/delete/{userId}/{festId}")
    suspend fun deleteFavorite(request: HttpServletRequest, @PathVariable userId: Long, @PathVariable festId:Long): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        try {
            val existingFavorite = service.getFavoriteIfExist(festId, userId).firstOrNull()

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
                    countName = "api.favoritefestive.deletefavorite.count",
                    distributionName = "api.favoritefestive.deletefavorite.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${FavoriteScope.PROTECTED}/user/delete/{userId}")
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
                    countName = "api.favoritefestive.deleteallfavoritebyuser.count",
                    distributionName = "api.favoritefestive.deleteallfavoritebyuser.latency"
                )
            )
        }
    }
}