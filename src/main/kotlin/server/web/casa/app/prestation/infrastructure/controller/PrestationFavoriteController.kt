package server.web.casa.app.prestation.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.ecosystem.application.service.PrestationService
import server.web.casa.app.prestation.application.FavoritePrestationService
import server.web.casa.app.prestation.domain.model.FavoritePrestationDTO
import server.web.casa.app.prestation.domain.request.FavoritePrestationRequest
import server.web.casa.app.prestation.infrastructure.persistance.entity.FavoritePrestationEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.favorite.PrestationFavoriteScope
import server.web.casa.utils.Mode
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Favorite Prestation", description = "Favorite Prestation's Management")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)

class PrestationFavoriteController(
    private val service: FavoritePrestationService,
    private val userS: UserService,
    private val presTS: PrestationService,
    private val sentry: SentryService,
) {

    @PostMapping("/{version}/${PrestationFavoriteScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createPrestationFavorite(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: FavoritePrestationRequest
    ): ResponseEntity<Map<String, Any?>>{
        val startNanos = System.nanoTime()
        try {
            val user = userS.findIdUser(request.userId)
                ?: return ResponseEntity.badRequest()
                .body(mapOf("error" to "User not found"))
            val prestation = presTS.getById(request.prestationId)
                    ?: return ResponseEntity.badRequest()
                    .body(mapOf("error" to "prestation not found"))
            val data = FavoritePrestationEntity(
                userId = user.userId,
                prestationId = prestation.id
            )
            val verify = service.findByPrestationIdAndUserId(prestation.id!!, user.userId!!)
            //val created = verify ?: service.create(data)
            val created = verify?.takeIf { it.isNotEmpty() } ?: service.create(data)
            return ResponseEntity.status(201).body(mapOf("data" to created))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.prestationfavorite.createprestationfavorite.count",
                    distributionName = "api.prestationfavorite.createprestationfavorite.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${PrestationFavoriteScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavortitePrestation(request: HttpServletRequest):ResponseEntity<Map<String,List<FavoritePrestationDTO>>>{
        val startNanos = System.nanoTime()
        try {
            val all = service.findAll()
            return ResponseEntity.ok().body(mapOf("data" to all))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.prestationfavorite.getallfavortiteprestation.count",
                    distributionName = "api.prestationfavorite.getallfavortiteprestation.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${PrestationFavoriteScope.PROTECTED}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationPrestationById(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<Map<String, FavoritePrestationDTO?>>{
        val startNanos = System.nanoTime()
        try {
            val find = service.findById(id)
            return ResponseEntity.ok().body(mapOf("data" to find))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.prestationfavorite.getreservationprestationbyid.count",
                    distributionName = "api.prestationfavorite.getreservationprestationbyid.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${PrestationFavoriteScope.PROTECTED}/user/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getByUserPrestation(request: HttpServletRequest, @PathVariable userId: Long): ResponseEntity<Map<String, List<FavoritePrestationDTO>?>>{
        val startNanos = System.nanoTime()
        try {
            val find = service.findByUserId( userId)
            return ResponseEntity.ok(mapOf("data" to find))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.prestationfavorite.getbyuserprestation.count",
                    distributionName = "api.prestationfavorite.getbyuserprestation.latency"
                )
            )
        }
    }
    @GetMapping("/{version}/${PrestationFavoriteScope.PROTECTED}/prestation/{prestationId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getByPrestationFavorite(request: HttpServletRequest, @PathVariable prestationId: Long): ResponseEntity<Map<String, List<FavoritePrestationDTO>?>>{
        val startNanos = System.nanoTime()
        try {
            val find = service.findByPrestationId(prestationId)
            return ResponseEntity.ok(mapOf("data" to find))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.prestationfavorite.getbyprestationfavorite.count",
                    distributionName = "api.prestationfavorite.getbyprestationfavorite.latency"
                )
            )
        }
    }
    @GetMapping("/{version}/${PrestationFavoriteScope.PROTECTED}/{userId}/{prestationId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getByUserPrestation(request: HttpServletRequest, @PathVariable userId: Long, @PathVariable prestationId: Long): ResponseEntity<Map<String, List<FavoritePrestationDTO>?>>{
        val startNanos = System.nanoTime()
        try {
             val find = service.findByPrestationIdAndUserId(prestationId, userId)
            return ResponseEntity.ok(mapOf("data" to find))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.prestationfavorite.getbyuserprestation.count",
                    distributionName = "api.prestationfavorite.getbyuserprestation.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${PrestationFavoriteScope.PROTECTED}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun deleteByIdFavoritePrestation(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<Map<String, String>>{
        val startNanos = System.nanoTime()
        try {
            val find = service.findById(id)
            if (find != null) service.deleteById(find.favorite.id!!) else return ResponseEntity.badRequest().body(mapOf("error" to "favorite not found"))
            return ResponseEntity.ok().body(mapOf("message" to "Favorite deleted"))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.prestationfavorite.deletebyidfavoriteprestation.count",
                    distributionName = "api.prestationfavorite.deletebyidfavoriteprestation.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${PrestationFavoriteScope.PROTECTED}/delete/all")
    suspend fun deleteAllFavoritePrestation(request: HttpServletRequest): ResponseEntity<Map<String, String>>{
        val startNanos = System.nanoTime()
        try {
            service.deleteAll()
            return ResponseEntity.ok(mapOf("message" to "All favorite prestation deleted"))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.prestationfavorite.deleteallfavoriteprestation.count",
                    distributionName = "api.prestationfavorite.deleteallfavoriteprestation.latency"
                )
            )
        }
    }
}
