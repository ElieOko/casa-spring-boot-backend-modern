package server.web.casa.app.pub.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.pub.application.PublicityService
import server.web.casa.app.pub.domain.model.PublicityRequest
import server.web.casa.app.pub.infrastructure.persistance.entity.PublicityEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.pub.PubScope
import server.web.casa.utils.Mode
import java.time.LocalDate
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Publicity", description = "Publicity's Management")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)
class PublicityController(
    private val service: PublicityService,
    private val userS: UserService,
    private val sentry: SentryService,
) {
    @PostMapping("/{version}/${PubScope.PROTECTED}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(httpRequest: HttpServletRequest,
@Valid @RequestBody request: PublicityRequest
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val user = userS.findIdUser(request.userId)
                    ?: return ResponseEntity.badRequest()
                    .body(mapOf("error" to "User not found")).also { statusCode = it.statusCode.value().toString() }

            val pub = PublicityEntity(
                user = user.userId,
                imagePath = request.imagePath,
                title = request.title,
                description = request.description,
                isActive = true,
                createdAt = LocalDate.now()
            )
            val pubCreate = service.createPub(pub)
            val response = mapOf(
                "message" to "pub create with success",
                "pub" to pubCreate,
                "user" to user
            )
            return ResponseEntity.status(201).body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.publicity.create.count",
                    distributionName = "api.publicity.create.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${PubScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPub(request: HttpServletRequest): ResponseEntity<Map<String, List<PublicityEntity>>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val pub = service.findAllPub()
            val response = mapOf("pub" to pub)
            return ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.publicity.getallpub.count",
                    distributionName = "api.publicity.getallpub.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${PubScope.PUBLIC}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getPubById(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<Map<String, PublicityEntity?>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val pub = service.findId(id)
            val response = mapOf("pub" to pub)
            return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.publicity.getpubbyid.count",
                    distributionName = "api.publicity.getpubbyid.latency"
                )
            )
        }
    }
    @GetMapping("/{version}/${PubScope.PROTECTED}/user/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getPubByUser(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<Map<String, List<PublicityEntity?>>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val user = userS.findIdUser(id) ?: throw RuntimeException("User not found")
            val pub = service.findByUser(user.userId!!)
            val response = mapOf("pub" to pub)
            return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.publicity.getpubbyuser.count",
                    distributionName = "api.publicity.getpubbyuser.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${PubScope.PROTECTED}/date/{createdAt}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getPubByDate(request: HttpServletRequest, @PathVariable createdAt: LocalDate): ResponseEntity<Map<String, List<PublicityEntity?>>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val pub = service.findByCreated(createdAt)
            val response = mapOf("pub" to pub)
            return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.publicity.getpubbydate.count",
                    distributionName = "api.publicity.getpubbydate.latency"
                )
            )
        }
    }
    @PutMapping("/{version}/${PubScope.PROTECTED}/update/active/{id}")
    suspend fun updateReservation(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody state: Boolean
    ): ResponseEntity<Map<String, PublicityEntity?>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val pub = service.findId(id)
                ?: throw RuntimeException("Publicity not found")
            val updated = service.updateIsActive(id, state )
            val pubUpdate = service.findId(id)
            return ResponseEntity.ok(mapOf("pub" to pubUpdate)).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.publicity.updatereservation.count",
                    distributionName = "api.publicity.updatereservation.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${PubScope.PROTECTED}/delete/{id}")
    suspend fun deletePub(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val pub = service.findId(id)
                ?: throw RuntimeException("Publicity not found")
            val deleted = service.deleteById(id)
            return ResponseEntity.ok(mapOf("message" to "publicity deleted")).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.publicity.deletepub.count",
                    distributionName = "api.publicity.deletepub.latency"
                )
            )
        }
    }
}