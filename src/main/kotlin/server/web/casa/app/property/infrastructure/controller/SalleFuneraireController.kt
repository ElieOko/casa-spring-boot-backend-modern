package server.web.casa.app.property.infrastructure.controller

import server.web.casa.route.GlobalRoute
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*
import server.web.casa.app.address.application.service.*
import server.web.casa.app.payment.application.service.*
import server.web.casa.app.property.application.service.*
import server.web.casa.app.property.domain.model.*
import server.web.casa.app.property.domain.model.request.*
import server.web.casa.app.user.application.service.*
import server.web.casa.route.property.PropertyFuneraireScope
import server.web.casa.utils.*
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Funeraire", description = "")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
class SalleFuneraireController(
    private val service: SalleFuneraireService,
    private val devise : DeviseService,
    private val userService: UserService,
    private val imageService: FuneraireImageService,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService,
    private val propertyTypeService: PropertyTypeService,
    private val sentry: SentryService,
) {
    @Operation(summary = "Création bureau")
    @PostMapping("/${PropertyFuneraireScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFuneraire(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: SalleFuneraireRequest,
    ) = coroutineScope{
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            if (request.funeraire.propertyTypeId != 7L) throw ResponseStatusException(HttpStatusCode.valueOf(404), "Ce type n'appartient pas au salle funeraire")
            propertyTypeService.findByIdPropertyType(request.funeraire.propertyTypeId)
            val city = if (request.funeraire.cityId != null) cityService.findByIdCity(request.funeraire.cityId) else null
            val commune = communeService.findByIdCommune(request.funeraire.communeId)
            val quartier =  if (request.funeraire.quartierId != null) quartierService.findByIdQuartier(request.funeraire.quartierId) else null
            devise.getById(request.funeraire.deviseId)
            request.funeraire.cityId =  city?.cityId
            request.funeraire.communeId = commune?.communeId
            request.funeraire.quartierId = quartier?.quartierId
            userService.findIdUser(request.funeraire.userId!!)
            if (request.images.isEmpty()) throw ResponseStatusException(HttpStatusCode.valueOf(404), "Precisez des images.")
            val data = service.create(request.funeraire.toDomain(), request.features)
            request.images.forEach { imageService.create(ImageRequestStandard(data.id!!,it.image)) }
            ApiResponseWithMessage(data = data, message = "Enregistrement réussie pour la proprièté funeraire")
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.sallefuneraire.createfuneraire.count",
                    distributionName = "api.sallefuneraire.createfuneraire.latency"
                )
            )
        }
    }

    @Operation(summary = "List des bureaux")
    @GetMapping("/${PropertyFuneraireScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFuneraire(request: HttpServletRequest) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val data = service.getAll()
            val response = mapOf("funeraires" to data)
            ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.sallefuneraire.getallfuneraire.count",
                    distributionName = "api.sallefuneraire.getallfuneraire.latency"
                )
            )
        }
    }

    @Operation(summary = "Get Salle Funeraire by User")
    @GetMapping("/${PropertyFuneraireScope.PROTECTED}/owner/{userId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFuneraireByUser(
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
                    countName = "api.sallefuneraire.getallfunerairebyuser.count",
                    distributionName = "api.sallefuneraire.getallfunerairebyuser.latency"
                )
            )
        }
    }
    @Operation(summary = "Modification Salle festive")
    @PutMapping("/${PropertyFuneraireScope.PROTECTED}/image/{funeraireId}")
    suspend fun updateFile(
        httpRequest: HttpServletRequest,
        @PathVariable("funeraireId") funeraireId : Long,
        @Valid @RequestBody request: ImageChange
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            service.findById(funeraireId)
            val result = if (request.images.isNotEmpty()) imageService.updateFile(funeraireId,request.images) else false
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
                    countName = "api.sallefuneraire.updatefile.count",
                    distributionName = "api.sallefuneraire.updatefile.latency"
                )
            )
        }
    }

    @Operation(summary = "Suppression Salle festive")
    @DeleteMapping("/${PropertyFuneraireScope.PROTECTED}/image/{funeraireId}")
    suspend fun deleteFile(
        httpRequest: HttpServletRequest,
        @PathVariable("funeraireId") funeraireId : Long,
        @Valid @RequestBody request: PropertyImagesRequest
    ) = coroutineScope{
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            service.findById(funeraireId)
            val result = if (request.propertyImage.isNotEmpty()) imageService.deleteFile(funeraireId,request.propertyImage) else false
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
                    countName = "api.sallefuneraire.deletefile.count",
                    distributionName = "api.sallefuneraire.deletefile.latency"
                )
            )
        }
    }

    @Operation(summary = "Modification Salle Funeraire")
    @PutMapping("/${PropertyFuneraireScope.PROTECTED}/owner/{funeraireId}")
    suspend fun updateFuneraire(
        httpRequest: HttpServletRequest,
        @PathVariable("funeraireId") funeraireId : Long,
        @Valid @RequestBody request: SalleFuneraireDTO
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            userService.findIdUser(request.userId!!)
            val bureau = service.findById(funeraireId)
            if (bureau.userId != request.userId) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet utilisateur n'appartient pas à la proprièté bureau.")
            val city = cityService.findByIdCity(request.cityId)
            val propertyType = propertyTypeService.findByIdPropertyType(request.propertyTypeId!!)
            val commune = communeService.findByIdCommune(request.communeId)
            val quartier =  quartierService.findByIdQuartier(request.quartierId)
            val data = request.toDomain()
            data.cityId = city?.cityId
            data.propertyTypeId = propertyType.propertyTypeId
            data.id = funeraireId
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
                    countName = "api.sallefuneraire.updatefuneraire.count",
                    distributionName = "api.sallefuneraire.updatefuneraire.latency"
                )
            )
        }
    }

    @Operation(summary = "Sold")
    @PutMapping("/${PropertyFuneraireScope.PROTECTED}/sold/{propertyId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun soldOutOrInFuneraire(
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
                    countName = "api.sallefuneraire.soldoutorinfuneraire.count",
                    distributionName = "api.sallefuneraire.soldoutorinfuneraire.latency"
                )
            )
        }
    }

    @Operation(summary = "Enable or disable")
    @PutMapping("/${PropertyFuneraireScope.PROTECTED}/enable/{propertyId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun toEnableOrDisableFuneraire(
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
                    countName = "api.sallefuneraire.toenableordisablefuneraire.count",
                    distributionName = "api.sallefuneraire.toenableordisablefuneraire.latency"
                )
            )
        }
    }
}