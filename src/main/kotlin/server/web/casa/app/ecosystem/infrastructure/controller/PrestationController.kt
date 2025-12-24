package server.web.casa.app.ecosystem.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import server.web.casa.app.address.application.service.CityService
import server.web.casa.app.address.application.service.CommuneService
import server.web.casa.app.address.application.service.QuartierService
import server.web.casa.app.ecosystem.application.service.PrestationImageService
import server.web.casa.app.ecosystem.application.service.PrestationService
import server.web.casa.app.ecosystem.domain.model.Prestation
import server.web.casa.app.ecosystem.domain.model.PrestationImage
import server.web.casa.app.ecosystem.domain.model.toDomain
import server.web.casa.app.ecosystem.domain.request.PrestationRequest
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.ecosystem.Service

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
    private val quartierService: QuartierService
) {
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
        val user = userService.findIdUser(service.userId)
        val devise = deviseService.getById(service.deviseId)
        if (images.isNotEmpty()){
            val entity = service.toDomain()
            val data = prestationService.create(entity)
            images.forEach {
                prestationImage.create(PrestationImage(prestationId = data.id!!, name = it.image), server = baseUrl)
            }
            val response = mapOf(
                "message" to "Enregistrement réussie avec succès",
            )
            return ResponseEntity.status(201).body(response)
        }
        val response = mapOf(
            "message" to "Précisez au moins une réalisation"
        )
        return ResponseEntity.badRequest().body(response)
    }

    @Operation(summary = "Voir les Prestaions service")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPrestation() = coroutineScope {
        val response = mapOf("prestations" to prestationService.getAllData().toList())
        ResponseEntity.ok().body(response)
    }
}