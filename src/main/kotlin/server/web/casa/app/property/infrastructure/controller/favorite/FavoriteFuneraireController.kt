package server.web.casa.app.property.infrastructure.controller.favorite

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.property.application.service.SalleFuneraireService
import server.web.casa.app.property.application.service.favorite.FavoriteFuneraireService
import server.web.casa.app.property.domain.model.favorite.*
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteFuneraireEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.favorite.FavoriteFuneraireScope
import server.web.casa.utils.*
import java.time.LocalDate
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Favorite Salle Funeraire", description = "Gestion des favorites")
@RestController
@Profile(Mode.DEV)
@RequestMapping("api")
class FavoriteFuneraireController(
    private val service: FavoriteFuneraireService,
    private val userS: UserService,
    private val salleS: SalleFuneraireService,
    private val sentry: SentryService,
) {
    @PostMapping("/{version}/${FavoriteFuneraireScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFavorite(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: FavoriteFuneraireRequest
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val user = userS.findIdUser(request.userId)
            val salle = salleS.findById(request.funeraireId)

            val favorite = FavoriteFuneraireEntity(
                userId = user.userId!!,
                createdAt = LocalDate.now(),
                funeraireId = salle.id!!
            )
            val existingFavorite = service.getFavoriteIfExist(salle.id!!, user.userId!!)
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
                    countName = "api.favoritefuneraire.createfavorite.count",
                    distributionName = "api.favoritefuneraire.createfavorite.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteFuneraireScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavorite(request: HttpServletRequest): ApiResponse<List<FavoriteFuneraireDTO>> {
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
                    countName = "api.favoritefuneraire.getallfavorite.count",
                    distributionName = "api.favoritefuneraire.getallfavorite.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteFuneraireScope.PROTECTED}/user/{user}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getUserFavoriteFuneraire(request: HttpServletRequest, @PathVariable user: Long):ResponseEntity<Map<String, List<FavoriteFuneraireDTO>?>> {
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
                    countName = "api.favoritefuneraire.getuserfavoritefuneraire.count",
                    distributionName = "api.favoritefuneraire.getuserfavoritefuneraire.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteFuneraireScope.PROTECTED}/{funeraireId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteById(request: HttpServletRequest, @PathVariable funeraireId: Long):ResponseEntity<Map<String, FavoriteFuneraireDTO>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val favorite = service.getById(funeraireId) ?: throw ResponseStatusException(
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
                    countName = "api.favoritefuneraire.getonefavoritebyid.count",
                    distributionName = "api.favoritefuneraire.getonefavoritebyid.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${FavoriteFuneraireScope.PROTECTED}/salle/{funeraireId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getOneFavoriteProperty(request: HttpServletRequest, @PathVariable funeraireId: Long):ResponseEntity<Map<String, List<FavoriteFuneraireDTO>?>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val salle = salleS.findById (funeraireId)
            val favorite = service.getFavoriteByFuneId(salle.id!!)
            val response = mapOf("data" to favorite)
            return ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.favoritefuneraire.getonefavoriteproperty.count",
                    distributionName = "api.favoritefuneraire.getonefavoriteproperty.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${FavoriteFuneraireScope.PROTECTED}/delete/{id}")
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
                    countName = "api.favoritefuneraire.deletefavorite.count",
                    distributionName = "api.favoritefuneraire.deletefavorite.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${FavoriteFuneraireScope.PROTECTED}/delete/all")
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
                    countName = "api.favoritefuneraire.deletefavoriteall.count",
                    distributionName = "api.favoritefuneraire.deletefavoriteall.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${FavoriteFuneraireScope.PROTECTED}/delete/{userId}/{funeraireId}")
    suspend fun deleteFavorite(request: HttpServletRequest, @PathVariable userId: Long, @PathVariable funeraireId:Long): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val existingFavorite = service.getFavoriteIfExist(funeraireId, userId).firstOrNull()

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
                    countName = "api.favoritefuneraire.deletefavorite.count",
                    distributionName = "api.favoritefuneraire.deletefavorite.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${FavoriteFuneraireScope.PROTECTED}/user/delete/{userId}")
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
                    countName = "api.favoritefuneraire.deleteallfavoritebyuser.count",
                    distributionName = "api.favoritefuneraire.deleteallfavoritebyuser.latency"
                )
            )
        }
    }
}