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
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Favorite BUREAU", description = "Gestion des favorites")
@RestController
@Profile(Mode.DEV)
@RequestMapping("api")
class FavoriteBureauController(
    private val service: FavoriteBureauService,
    private val userS: UserService,
    private val brxS: BureauService,
    private val sentry: SentryService,
) {
    @PostMapping("/{version}/${FavoriteBureauScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFavorite(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: FavoriteBureauRequest
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
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
            return ResponseEntity.status(HttpStatus.CREATED).body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.favoritebureau.createfavorite.count",
                    distributionName = "api.favoritebureau.createfavorite.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteBureauScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavorite(request: HttpServletRequest): ApiResponse<List<FavoriteBureauDTO>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val data = service.getAll()
            return ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritebureau.getallfavorite.count",
                    distributionName = "api.favoritebureau.getallfavorite.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteBureauScope.PROTECTED}/user/{user}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getUserFavoriteProperty(request: HttpServletRequest, @PathVariable user: Long):ResponseEntity<Map<String, List<FavoriteBureauDTO>?>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val user = userS.findIdUser(user)
            val favorite = service.getUserFavorite(user.userId!!)
            val response = mapOf("data" to favorite)
            return ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritebureau.getuserfavoriteproperty.count",
                    distributionName = "api.favoritebureau.getuserfavoriteproperty.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteBureauScope.PROTECTED}/{festId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteById(request: HttpServletRequest, @PathVariable festId: Long):ResponseEntity<Map<String, FavoriteBureauDTO>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val favorite = service.getById(festId) ?: throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "favorite Not Found."
            )
            val response = mapOf("favorites" to favorite)
            return ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritebureau.getonefavoritebyid.count",
                    distributionName = "api.favoritebureau.getonefavoritebyid.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteBureauScope.PROTECTED}/salle/{festId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getFavoriteOneFestive(request: HttpServletRequest, @PathVariable festId: Long):ResponseEntity<Map<String, List<FavoriteBureauDTO>?>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val salle = brxS.findById (festId)
            val favorite = service.getFavoriteByFestId(salle.id!!)
            val response = mapOf("data" to favorite)
            return ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritebureau.getfavoriteonefestive.count",
                    distributionName = "api.favoritebureau.getfavoriteonefestive.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${FavoriteBureauScope.PROTECTED}/delete/{id}")
    suspend fun deleteFavorite(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            service.deleteById(id)
            val response = mapOf("message" to "Favorite deleted successfully")
            return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritebureau.deletefavorite.count",
                    distributionName = "api.favoritebureau.deletefavorite.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${FavoriteBureauScope.PROTECTED}/delete/all")
    suspend fun deleteFavoriteAll(request: HttpServletRequest): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            service.deleteAll()
            val response = mapOf("message" to "Favorite deleted successfully")
            return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritebureau.deletefavoriteall.count",
                    distributionName = "api.favoritebureau.deletefavoriteall.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${FavoriteBureauScope.PROTECTED}/delete/{userId}/{festId}")
    suspend fun deleteFavorite(request: HttpServletRequest, @PathVariable userId: Long, @PathVariable festId:Long): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val existingFavorite = service.getFavoriteIfExist(festId, userId).firstOrNull()

            val deleteFavorite = existingFavorite?.favorite?.id?.let {
                service.deleteById(it)
            }
            val response = mapOf("message" to "Favorite deleted successfully")
            return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritebureau.deletefavorite.count",
                    distributionName = "api.favoritebureau.deletefavorite.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${FavoriteBureauScope.PROTECTED}/user/delete/{userId}")
    suspend fun deleteAllFavoriteByUser(request: HttpServletRequest, @PathVariable userId: Long): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val user = userS.findIdUser(userId)
            service.deleteAllFavoriteUser(user.userId!!)
            val response = mapOf("message" to "Favorite deleted successfully")
            return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritebureau.deleteallfavoritebyuser.count",
                    distributionName = "api.favoritebureau.deleteallfavoritebyuser.latency"
                )
            )
        }
    }
}