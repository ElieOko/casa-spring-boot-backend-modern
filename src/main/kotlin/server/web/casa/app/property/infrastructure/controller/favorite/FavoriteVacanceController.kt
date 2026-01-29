package server.web.casa.app.property.infrastructure.controller.favorite

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.property.application.service.VacanceService
import server.web.casa.app.property.application.service.favorite.FavoriteVacanceService
import server.web.casa.app.property.domain.model.favorite.*
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteVacanceEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.favorite.FavoriteVacanceScope
import server.web.casa.utils.*
import java.time.LocalDate
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Favorite Terrain", description = "Gestion des favorites")
@RestController
@Profile(Mode.DEV)
@RequestMapping("api")
class FavoriteVacanceController(
    private val service: FavoriteVacanceService,
    private val userS: UserService,
    private val vacS: VacanceService,
    private val sentry: SentryService,
) {
    @PostMapping("/{version}/${FavoriteVacanceScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFavorite(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: FavoriteVacanceRequest
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val user = userS.findIdUser(request.userId)
            val vac = vacS.getAllVacance().find{ it.id == request.vacanceId } ?: throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "Vacance not found."
            )
            val favorite = FavoriteVacanceEntity(
                userId = user.userId!!,
                createdAt = LocalDate.now(),
                vacanceId = vac.id!!
            )
            val existingFavorite = service.getFavoriteIfExist(vac.id, user.userId)
            val savedFavorite = existingFavorite.ifEmpty { service.create(favorite) }

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
                    countName = "api.favoritevacance.createfavorite.count",
                    distributionName = "api.favoritevacance.createfavorite.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteVacanceScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavorite(request: HttpServletRequest): ApiResponse<List<FavoriteVacanceDTO>> {
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
                    countName = "api.favoritevacance.getallfavorite.count",
                    distributionName = "api.favoritevacance.getallfavorite.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteVacanceScope.PROTECTED}/user/{user}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getUserFavoriteVacance(request: HttpServletRequest, @PathVariable user: Long):ResponseEntity<Map<String, List<FavoriteVacanceDTO>?>> {
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
                    countName = "api.favoritevacance.getuserfavoritevacance.count",
                    distributionName = "api.favoritevacance.getuserfavoritevacance.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteVacanceScope.PROTECTED}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteById(request: HttpServletRequest, @PathVariable id: Long):ResponseEntity<Map<String, FavoriteVacanceDTO>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val favorite = service.getById (id) ?: throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "favorite Not Found."
            )
            val response = mapOf("data" to favorite)
            return ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritevacance.getonefavoritebyid.count",
                    distributionName = "api.favoritevacance.getonefavoritebyid.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteVacanceScope.PROTECTED}/salle/{vacanceId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteVacance(request: HttpServletRequest, @PathVariable vacanceId: Long):ResponseEntity<Map<String, List<FavoriteVacanceDTO>?>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val vac = vacS.getAllVacance().find{ it.id == vacanceId } ?: throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "Vacance not found."
            )
            val favorite = service.getFavoriteByVacanceId(vacanceId)
            val response = mapOf("data" to favorite)
            return ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritevacance.getonefavoritevacance.count",
                    distributionName = "api.favoritevacance.getonefavoritevacance.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${FavoriteVacanceScope.PROTECTED}/delete/{id}")
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
                    countName = "api.favoritevacance.deletefavorite.count",
                    distributionName = "api.favoritevacance.deletefavorite.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${FavoriteVacanceScope.PROTECTED}/delete/all")
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
                    countName = "api.favoritevacance.deletefavoriteall.count",
                    distributionName = "api.favoritevacance.deletefavoriteall.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${FavoriteVacanceScope.PROTECTED}/delete/{userId}/{id}")
    suspend fun deleteFavorite(request: HttpServletRequest, @PathVariable userId: Long, @PathVariable id:Long): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val existingFavorite = service.getFavoriteIfExist(id, userId).firstOrNull()

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
                    countName = "api.favoritevacance.deletefavorite.count",
                    distributionName = "api.favoritevacance.deletefavorite.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${FavoriteVacanceScope.PROTECTED}/user/delete/{userId}")
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
                    countName = "api.favoritevacance.deleteallfavoritebyuser.count",
                    distributionName = "api.favoritevacance.deleteallfavoritebyuser.latency"
                )
            )
        }
    }
}