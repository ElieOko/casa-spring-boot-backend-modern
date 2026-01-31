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
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Favorite Terrain", description = "Gestion des favorites")
@RestController
@Profile(Mode.DEV)
@RequestMapping("api")
class FavoriteTerrainController(
    private val service: FavoriteTerrainService,
    private val userS: UserService,
    private val terS: TerrainService,
    private val sentry: SentryService,
) {
    @PostMapping("/{version}/${FavoriteTerrainScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFavorite(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: FavoriteTerrainRequest
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
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
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.favoriteterrain.createfavorite.count",
                    distributionName = "api.favoriteterrain.createfavorite.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteTerrainScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavorite(request: HttpServletRequest): ApiResponse<List<FavoriteTerrainDTO>> {
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
                    countName = "api.favoriteterrain.getallfavorite.count",
                    distributionName = "api.favoriteterrain.getallfavorite.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteTerrainScope.PROTECTED}/user/{user}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getUserFavoriteTerrain(request: HttpServletRequest, @PathVariable user: Long):ResponseEntity<Map<String, List<FavoriteTerrainDTO>?>> {
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
                    countName = "api.favoriteterrain.getuserfavoriteterrain.count",
                    distributionName = "api.favoriteterrain.getuserfavoriteterrain.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteTerrainScope.PROTECTED}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteById(request: HttpServletRequest, @PathVariable id: Long):ResponseEntity<Map<String, FavoriteTerrainDTO>> {
        val startNanos = System.nanoTime()
        try {
            val favorite = service.getById (id) ?: throw ResponseStatusException(
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
                    countName = "api.favoriteterrain.getonefavoritebyid.count",
                    distributionName = "api.favoriteterrain.getonefavoritebyid.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteTerrainScope.PROTECTED}/salle/{terrainId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteProperty(request: HttpServletRequest, @PathVariable terrainId: Long):ResponseEntity<Map<String, List<FavoriteTerrainDTO>?>> {
        val startNanos = System.nanoTime()
        try {
            val hotel = terS.findById( terrainId )
            val favorite = service.getFavoriteByTerrainId(terrainId)
            val response = mapOf("data" to favorite)
            return ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoriteterrain.getonefavoriteproperty.count",
                    distributionName = "api.favoriteterrain.getonefavoriteproperty.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${FavoriteTerrainScope.PROTECTED}/delete/{id}")
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
                    countName = "api.favoriteterrain.deletefavorite.count",
                    distributionName = "api.favoriteterrain.deletefavorite.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${FavoriteTerrainScope.PROTECTED}/delete/all")
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
                    countName = "api.favoriteterrain.deletefavoriteall.count",
                    distributionName = "api.favoriteterrain.deletefavoriteall.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${FavoriteTerrainScope.PROTECTED}/delete/{userId}/{id}")
    suspend fun deleteFavorite(request: HttpServletRequest, @PathVariable userId: Long, @PathVariable id:Long): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        try {
            val existingFavorite = service.getFavoriteIfExist(id, userId).firstOrNull()

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
                    countName = "api.favoriteterrain.deletefavorite.count",
                    distributionName = "api.favoriteterrain.deletefavorite.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${FavoriteTerrainScope.PROTECTED}/user/delete/{userId}")
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
                    countName = "api.favoriteterrain.deleteallfavoritebyuser.count",
                    distributionName = "api.favoriteterrain.deleteallfavoritebyuser.latency"
                )
            )
        }
    }
}