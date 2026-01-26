package server.web.casa.app.ecosystem.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import server.web.casa.app.address.application.service.*
import server.web.casa.app.ecosystem.application.service.*
import server.web.casa.app.ecosystem.domain.model.PrestationImage
import server.web.casa.app.ecosystem.domain.model.toDomain
import server.web.casa.app.ecosystem.domain.request.PrestationRequest
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.ecosystem.Service
import server.web.casa.security.Auth
import server.web.casa.utils.ApiResponse

@Tag(name = "Prestation Service", description = "Gestion des prestations services")
@RestController
@RequestMapping(Service.PRESTATION)
class PrestationController(
    private val userService: UserService,
    private val deviseService: DeviseService,
    private val prestationImage : PrestationImageService,
    private val prestationService : PrestationService,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService,
    private val auth: Auth,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createPrestationService(
        @Valid @RequestBody request: PrestationRequest,
        requestHttp: HttpServletRequest
    ): ResponseEntity<Map<String, String>> {
        val commune = communeService.findByIdCommune(request.prestation.communeId)
        val quartier =  quartierService.findByIdQuartier(request.prestation.quartierId)
        val city = cityService.findByIdCity(request.prestation.cityId)
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
            return ResponseEntity.status(201).body(response)
        }
        val response = mapOf("message" to "Précisez au moins une réalisation")
        return ResponseEntity.badRequest().body(response)
    }

    @Operation(summary = "Voir les Prestaions service")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPrestation() = coroutineScope {
        val user = auth.user()
        val state: Boolean? = user?.second?.find{ true }
        val message = when (state) {
         true -> mapOf("prestations" to prestationService.getAllData2().toList())
         false,null -> mapOf("prestations" to prestationService.getAllData().toList())
        }
        ResponseEntity.ok().body(message)
    }

    @Operation(summary = "Voir les Prestaions service")
    @GetMapping("/api/dashb",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPrestationAdmin() = coroutineScope {
        val user = auth.user()
        val state: Boolean? = user?.second?.find{ true }
        val message = when (state) {
            true -> mapOf("prestations" to prestationService.getAllData2().toList())
            false,null -> mapOf("prestations" to prestationService.getAllData().toList())
        }
        ResponseEntity.ok().body(message)
    }

    @Operation(summary = "Get Prestaion by ID")
    @GetMapping("/{prestationId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPrestaionById(
        @PathVariable("prestationId") prestationId : Long,
    ) = coroutineScope {
        val data = prestationService.getByIdPrestation(prestationId)
        ApiResponse(data)
    }

    @Operation(summary = "Get Prestaion by User")
    @GetMapping("/owner/{userId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPrestaionByUser(
        @PathVariable("userId") userId : Long,
    ) = coroutineScope {
        val data = prestationService.getAllPrestationByUser(userId)
        ApiResponse(data)
    }
}