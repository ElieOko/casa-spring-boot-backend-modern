package server.web.casa.app.property.infrastructure.controller

import server.web.casa.route.GlobalRoute
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.address.application.service.*
import server.web.casa.app.payment.application.service.*
import server.web.casa.app.property.application.service.*
import server.web.casa.app.property.domain.model.*
import server.web.casa.app.property.domain.model.request.*
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.property.PropertyFestiveScope
import server.web.casa.utils.*
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Festive", description = "")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
class SalleFestiveController(
    private val service: SalleFestiveService,
    private val devise : DeviseService,
    private val userService: UserService,
    private val imageService: FestiveImageService,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService,
    private val propertyTypeService: PropertyTypeService,
    private val sentry: SentryService,
) {
    @Operation(summary = "Création salle festive")
    @PostMapping("/${PropertyFestiveScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFestive(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: SalleFestiveRequest,
    ) = coroutineScope{
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            if (request.festive.propertyTypeId != 8L) throw ResponseStatusException(HttpStatusCode.valueOf(404), "Ce type n'appartient pas au salle de fête")
            propertyTypeService.findByIdPropertyType(request.festive.propertyTypeId)
            val city = if (request.festive.cityId != null) cityService.findByIdCity(request.festive.cityId) else null
            val commune = communeService.findByIdCommune(request.festive.communeId)
            val quartier =  if (request.festive.quartierId != null) quartierService.findByIdQuartier(request.festive.quartierId) else null
            request.festive.cityId =  city?.cityId
            request.festive.communeId = commune?.communeId
            request.festive.quartierId = quartier?.quartierId
            devise.getById(request.festive.deviseId)
            userService.findIdUser(request.festive.userId!!)
            if (request.images.isEmpty()) throw ResponseStatusException(HttpStatusCode.valueOf(404), "Precisez des images.")
            val data = service.create(request.festive.toDomain(), request.features)
            request.images.forEach { imageService.create(ImageRequestStandard(data.id!!,it.image)) }
            ApiResponseWithMessage(data = data, message = "Enregistrement réussie pour la proprièté festive")
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.sallefestive.createfestive.count",
                    distributionName = "api.sallefestive.createfestive.latency"
                )
            )
        }
    }

    @Operation(summary = "Listes des salles festives")
    @GetMapping("/${PropertyFestiveScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFestive(request: HttpServletRequest) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val data = service.getAll()
            val response = mapOf("festives" to data)
            ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.sallefestive.getallfestive.count",
                    distributionName = "api.sallefestive.getallfestive.latency"
                )
            )
        }
    }

    @Operation(summary = "Get Salle Festive by User")
    @GetMapping("/${PropertyFestiveScope.PROTECTED}/owner/{userId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFestiveByUser(
        request: HttpServletRequest,
        @PathVariable("userId") userId : Long,
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val data = service.getAllPropertyByUser(userId)
            ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.sallefestive.getallfestivebyuser.count",
                    distributionName = "api.sallefestive.getallfestivebyuser.latency"
                )
            )
        }
    }
    @Operation(summary = "Modification Salle Festive image")
    @PutMapping("/${PropertyFestiveScope.PROTECTED}/image/{festiveId}")
    suspend fun updateFileFestive(
        httpRequest: HttpServletRequest,
        @PathVariable("festiveId") festiveId : Long,
        @Valid @RequestBody request: ImageChange
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            service.findById(festiveId)
            val result = if (request.images.isNotEmpty()) imageService.updateFile(festiveId,request.images) else false
            val message = mutableMapOf("message" to "Modification effectuée avec succès")
            if (result)  ResponseEntity.ok(message).also { statusCode = it.statusCode.value().toString() } else {
                message["message"] = "Aucune modification n'a été effectuée"
                ResponseEntity.badRequest().body(message).also { statusCode = it.statusCode.value().toString() }
            }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.sallefestive.updatefilefestive.count",
                    distributionName = "api.sallefestive.updatefilefestive.latency"
                )
            )
        }
    }
    @Operation(summary = "Suppression Salle Festive image")
    @DeleteMapping("/${PropertyFestiveScope.PROTECTED}/image/{festiveId}")
    suspend fun deleteFile(
        httpRequest: HttpServletRequest,
        @PathVariable("festiveId") festiveId : Long,
        @Valid @RequestBody request: PropertyImagesRequest
    ) = coroutineScope{
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            service.findById(festiveId)
            val result = if (request.propertyImage.isNotEmpty()) imageService.deleteFile(festiveId,request.propertyImage) else false
            val message = mutableMapOf("message" to "Suppression effectuée avec succès")
            if (result)  ResponseEntity.ok(message).also { statusCode = it.statusCode.value().toString() } else {
                message["message"] = "Aucune suppression n'a été effectuée"
                ResponseEntity.badRequest().body(message).also { statusCode = it.statusCode.value().toString() }
            }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.sallefestive.deletefile.count",
                    distributionName = "api.sallefestive.deletefile.latency"
                )
            )
        }
    }

    @Operation(summary = "Modification Salle Festive")
    @PutMapping("/${PropertyFestiveScope.PROTECTED}/owner/{festiveId}")
    suspend fun updateBureau(
        httpRequest: HttpServletRequest,
        @PathVariable("festiveId") festiveId : Long,
        @Valid @RequestBody request: SalleFestiveDTO
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            userService.findIdUser(request.userId!!)
            val bureau = service.findById(festiveId)
            if (bureau.userId != request.userId) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet utilisateur n'appartient pas à la proprièté bureau.")
            val city = cityService.findByIdCity(request.cityId)
            val propertyType = propertyTypeService.findByIdPropertyType(request.propertyTypeId!!)
            val commune = communeService.findByIdCommune(request.communeId)
            val quartier =  quartierService.findByIdQuartier(request.quartierId)
            val data = request.toDomain()
            data.cityId = city?.cityId
            data.propertyTypeId = propertyType.propertyTypeId
            data.id = festiveId
            data.communeId = commune?.communeId
            data.quartierId = quartier?.quartierId
            service.update(data)
            val message = mutableMapOf("message" to "Modification effectuée avec succès")
            ResponseEntity.ok(message).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.sallefestive.updatebureau.count",
                    distributionName = "api.sallefestive.updatebureau.latency"
                )
            )
        }
    }

    @Operation(summary = "Sold")
    @PutMapping("/${PropertyFestiveScope.PROTECTED}/sold/{propertyId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun soldOutOrInFestive(
        httpRequest: HttpServletRequest,
        @PathVariable("propertyId") propertyId : Long,
        @RequestBody request : StatusState
    )= coroutineScope{
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val message = mutableMapOf("message" to if(request.status) "Proprièté bouqué(soldout) avec succès" else "Proprièté non bouqué(soldin) avec succès")
            val data = service.findById(propertyId)
            data.sold = request.status
            service.createOrUpdate(data)
            ResponseEntity.badRequest().body(message).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.sallefestive.soldoutorinfestive.count",
                    distributionName = "api.sallefestive.soldoutorinfestive.latency"
                )
            )
        }
    }

    @Operation(summary = "Enable or disable")
    @PutMapping("/${PropertyFestiveScope.PROTECTED}/enable/{propertyId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun toEnableOrDisableFestive(
        httpRequest: HttpServletRequest,
        @PathVariable("propertyId") propertyId : Long,
        @RequestBody request : StatusState
    )= coroutineScope{
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val message = mutableMapOf("message" to if(request.status) "Proprièté activé avec succès" else "Proprièté desactivé avec succès")
            val data= service.findById(propertyId)
            data.isAvailable = request.status
            service.createOrUpdate(data)
            ResponseEntity.badRequest().body(message).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.sallefestive.toenableordisablefestive.count",
                    distributionName = "api.sallefestive.toenableordisablefestive.latency"
                )
            )
        }
    }
}