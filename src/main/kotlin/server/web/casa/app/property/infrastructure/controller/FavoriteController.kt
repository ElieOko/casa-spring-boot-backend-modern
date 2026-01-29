package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.property.application.service.*
import server.web.casa.app.property.domain.model.FavoriteDTO
import server.web.casa.app.property.domain.model.request.FavoriteRequest
import server.web.casa.app.property.infrastructure.persistence.entity.FavoriteEntity
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyRepository
import server.web.casa.app.user.application.service.UserService
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import server.web.casa.route.GlobalRoute
import server.web.casa.route.favorite.PropertyFavoriteScope
import server.web.casa.utils.*
import java.time.LocalDate
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Favorite", description = "Gestion des favorites")
@RestController
@Profile(Mode.DEV)
@RequestMapping("${GlobalRoute.ROOT}/{version}")

class FavoriteController(
    private val service: FavoriteService,
    private val userS: UserService,
    private val userR: UserRepository,
    private val prop: PropertyService,
    private val propR: PropertyRepository,
    private val sentry: SentryService,
) {

    @PostMapping("/${PropertyFavoriteScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFavorite(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: FavoriteRequest
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
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
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.favorite.createfavorite.count",
                    distributionName = "api.favorite.createfavorite.latency"
                )
            )
        }
    }

    @GetMapping("/${PropertyFavoriteScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavorite(request: HttpServletRequest): ApiResponse<List<FavoriteDTO>> {
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
                    countName = "api.favorite.getallfavorite.count",
                    distributionName = "api.favorite.getallfavorite.latency"
                )
            )
        }
    }

    @GetMapping("/${PropertyFavoriteScope.PROTECTED}/user/{user}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getUserFavoriteProperty(request: HttpServletRequest, @PathVariable user: Long):ResponseEntity<Map<String, List<FavoriteDTO>?>> {
        val startNanos = System.nanoTime()
        try {
            val user = userR.findById(user) ?: throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "ID Is Not Found."
            )
            val favorite = service.getUserFavoriteProperty(user.userId!!)
            val response = mapOf("favorites" to favorite)
            return ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favorite.getuserfavoriteproperty.count",
                    distributionName = "api.favorite.getuserfavoriteproperty.latency"
                )
            )
        }
    }

    @GetMapping("/${PropertyFavoriteScope.PROTECTED}/{favoriteId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteById(request: HttpServletRequest, @PathVariable favoriteId: Long):ResponseEntity<Map<String, FavoriteDTO>> {
        val startNanos = System.nanoTime()
        try {
            val favorite = service.getById(favoriteId) ?: throw ResponseStatusException(
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
                    countName = "api.favorite.getonefavoritebyid.count",
                    distributionName = "api.favorite.getonefavoritebyid.latency"
                )
            )
        }
    }

    @GetMapping("/${PropertyFavoriteScope.PROTECTED}/property/{property}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteProperty(request: HttpServletRequest, @PathVariable property: Long):ResponseEntity<Map<String, List<FavoriteDTO>?>> {
        val startNanos = System.nanoTime()
        try {
            val property = propR.findById (property) ?: throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "ID Is Not Found."
            )
            val favorite = service.getOneFavoritePropertyCount(property.id!!)
            val response = mapOf("favorites" to favorite)
            return ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favorite.getonefavoriteproperty.count",
                    distributionName = "api.favorite.getonefavoriteproperty.latency"
                )
            )
        }
    }

    @DeleteMapping("/${PropertyFavoriteScope.PROTECTED}/delete/{id}")
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
                    countName = "api.favorite.deletefavorite.count",
                    distributionName = "api.favorite.deletefavorite.latency"
                )
            )
        }
    }
    @DeleteMapping("/${PropertyFavoriteScope.PROTECTED}/delete/all")
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
                    countName = "api.favorite.deletefavoriteall.count",
                    distributionName = "api.favorite.deletefavoriteall.latency"
                )
            )
        }
    }
    @DeleteMapping("/${PropertyFavoriteScope.PROTECTED}/delete/{userId}/{propertyId}")
    suspend fun deleteFavorite(request: HttpServletRequest, @PathVariable userId: Long, @PathVariable propertyId:Long): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        try {
            val existingFavorite = service.getFavoriteIfExist(propertyId, userId)?.firstOrNull()

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
                    countName = "api.favorite.deletefavorite.count",
                    distributionName = "api.favorite.deletefavorite.latency"
                )
            )
        }
    }

    @DeleteMapping("/${PropertyFavoriteScope.PROTECTED}/user/delete/{userId}")
    suspend fun deleteAllFavoriteByUser(request: HttpServletRequest, @PathVariable userId: Long): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        try {
            val user = userR.findById(userId) ?: throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "User not found."
            )
            service.deleteAllFavoriteUser(user.userId!!)
            val response = mapOf("message" to "Favorite deleted successfully")
            return ResponseEntity.ok(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favorite.deleteallfavoritebyuser.count",
                    distributionName = "api.favorite.deleteallfavoritebyuser.latency"
                )
            )
        }
    }
}