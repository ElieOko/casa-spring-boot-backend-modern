package server.web.casa.app.ecosystem.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.address.application.service.CityService
import server.web.casa.app.address.application.service.CommuneService
import server.web.casa.app.address.application.service.QuartierService
import server.web.casa.app.ecosystem.application.service.PrestationImageService
import server.web.casa.app.ecosystem.application.service.PrestationService
import server.web.casa.app.ecosystem.domain.model.PrestationImage
import server.web.casa.app.ecosystem.domain.model.PrestationRequestUpdate
import server.web.casa.app.ecosystem.domain.model.toDomain
import server.web.casa.app.ecosystem.domain.request.PrestationRequest
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.PrestationEntity
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.user.application.service.UserService
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import server.web.casa.route.ecosystem.PrestationScope
import server.web.casa.security.Auth
import server.web.casa.security.monitoring.MetricModel
import server.web.casa.security.monitoring.SentryService
import server.web.casa.utils.ApiResponse
import server.web.casa.utils.MessageResponse

@Tag(name = "Prestation Service", description = "Gestion des prestations services")
@RestController
@RequestMapping("api")
class PrestationController(
    private val userService: UserService,
    private val deviseService: DeviseService,
    private val prestationImage : PrestationImageService,
    private val prestationService : PrestationService,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService,
    private val auth: Auth,
    private val sentry: SentryService,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    @PostMapping("/{version}/${PrestationScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createPrestationService(
        @Valid @RequestBody request: PrestationRequest,
        requestHttp: HttpServletRequest
    ): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        val userConnect = auth.user()
        try {
            if (userConnect?.first?.isCertified != true) throw ResponseStatusException(HttpStatusCode.valueOf(403), MessageResponse.ACCOUNT_NOT_CERTIFIED)
            communeService.findByIdCommune(request.prestation.communeId)
            quartierService.findByIdQuartier(request.prestation.quartierId)
            cityService.findByIdCity(request.prestation.cityId)
            val isProd = true
            val baseUrl = if (isProd) "${requestHttp.scheme}://${requestHttp.serverName}"  else  "${requestHttp.scheme}://${requestHttp.serverName}:${requestHttp.serverPort}"
            val images = request.images
            val service = request.prestation
            userService.findIdUser(service.userId)
            deviseService.getById(service.deviseId)
            if (images.isNotEmpty()){
                val entity = service.toDomain()
                val data = prestationService.create(entity)
                images.forEach { prestationImage.create(PrestationImage(prestationId = data.id!!, name = it.image), server = baseUrl) }
                val response = mapOf("message" to "Enregistrement réussie avec succès",)
                return ResponseEntity.status(201).body(response)}
            val response = mapOf("message" to "Précisez au moins une réalisation")
            return ResponseEntity.badRequest().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${requestHttp.method} /${requestHttp.requestURI}",
                    countName = "api.prestation.createprestationservice.count",
                    distributionName = "api.prestation.createprestationservice.latency"
                )
            )
        }
    }

    @Operation(summary = "Voir les Prestaions service")
    @GetMapping("/{version}/${PrestationScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPrestation(request: HttpServletRequest) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val message = mapOf("prestations" to prestationService.getAllData().toList())
            ResponseEntity.ok().body(message)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.prestation.getallprestation.count",
                    distributionName = "api.prestation.getallprestation.latency"
                )
            )
        }
    }

    @Operation(summary = "Voir les Prestaions service")
    @GetMapping("/{version}/${PrestationScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPrestationAdmin(request: HttpServletRequest) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val user = auth.user()
            val state: Boolean? = user?.second?.find{ true }
            val message = when (state) {
                true -> mapOf("prestations" to prestationService.getAllData2().toList())
                false,null -> mapOf("prestations" to "Vous n'avez pas accès")
            }
            ResponseEntity.ok().body(message)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.prestation.getallprestationadmin.count",
                    distributionName = "api.prestation.getallprestationadmin.latency"
                )
            )
        }
    }

    @Operation(summary = "Get Prestaion by ID")
    @GetMapping("/{version}/${PrestationScope.PUBLIC}/{prestationId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPrestaionById(
        request: HttpServletRequest,
        @PathVariable("prestationId") prestationId : Long,
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = prestationService.getByIdPrestation(prestationId)
            ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.prestation.getallprestaionbyid.count",
                    distributionName = "api.prestation.getallprestaionbyid.latency"
                )
            )
        }
    }

    @Operation(summary = "Get Prestaion by User")
    @GetMapping("/{version}/${PrestationScope.PROTECTED}/owner/{userId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPrestaionByUser(
        request: HttpServletRequest,
        @PathVariable("userId") userId : Long,
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = prestationService.getAllPrestationByUser(userId)
            ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.prestation.getallprestaionbyuser.count",
                    distributionName = "api.prestation.getallprestaionbyuser.latency"
                )
            )
        }
    }
    @Operation(summary = "Update Prestation all column")
    @PutMapping("/{version}/${PrestationScope.PRIVATE}/update/{id}")
    suspend fun updatePrestation(
        httpRequest: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody request: PrestationRequestUpdate
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val prestation: PrestationEntity = (prestationService.getById(id) ?: return@coroutineScope ResponseEntity.ok().body(mapOf("errorr" to "Prestation not found"))) as PrestationEntity
            if ( prestation.id != request.id) return@coroutineScope ResponseEntity.ok().body(mapOf("error" to "Les id prestation ne correspondent pas")) else null

            val checkAdmin = userService.isAdmin()
            val userRequest: UserEntity = (userService.findId(request.userId) ?: return@coroutineScope  ResponseEntity.ok(mapOf("error" to "user not found"))) as UserEntity
            val userId = prestation.userId

            val proprioCheck = userRequest.userId == userId

            if( !proprioCheck && !checkAdmin.first){
               return@coroutineScope ResponseEntity.ok(mapOf("error" to "Authorization denied"))
            }
            val updated = prestationService.updatePrestation(request, id)
            ResponseEntity.ok(mapOf("data" to updated))

        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.prestation.updateprestation.count",
                    distributionName = "api.prestation.updateprestation.latency"
                )
            )
        }
    }
}