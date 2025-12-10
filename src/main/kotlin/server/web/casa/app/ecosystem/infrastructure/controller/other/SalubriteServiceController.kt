package server.web.casa.app.ecosystem.infrastructure.controller.other

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.address.application.service.*
import server.web.casa.app.address.infrastructure.persistence.mapper.toEntity
import server.web.casa.app.ecosystem.application.service.realisation.RealisationSalubriteServiceAction
import server.web.casa.app.ecosystem.application.service.task.SalubriteServiceAction
import server.web.casa.app.ecosystem.domain.model.realisation.SalubriteRealisation
import server.web.casa.app.ecosystem.domain.model.task.SalubriteTask
import server.web.casa.app.ecosystem.domain.request.SalubriteServiceRequest
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.salubrite.ServiceSalubriteEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.salubrite.toDomain
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.payment.domain.model.toEntity
import server.web.casa.app.property.domain.model.Property
import server.web.casa.app.user.application.service.UserService
import server.web.casa.app.user.infrastructure.persistence.mapper.toEntityToDto
import server.web.casa.route.ecosystem.Service

@Tag(name = "Salubrite Service", description = "Gestion des services Salubrite")
@RestController
@RequestMapping(Service.SALUBRITE)
class SalubriteServiceController(
    private val userService: UserService,
    private val deviseService: DeviseService,
    private val realisationImage : RealisationSalubriteServiceAction,
    private val serviceAction : SalubriteServiceAction,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createSalubriteService(
        @Valid @RequestBody request: SalubriteServiceRequest,
        requestHttp: HttpServletRequest
    ): ResponseEntity<Map<String, String>> {
        val isProd = true
        val baseUrl = if (isProd) "${requestHttp.scheme}://${requestHttp.serverName}"  else  "${requestHttp.scheme}://${requestHttp.serverName}:${requestHttp.serverPort}"
        val realisation = request.realisation
        val service = request.service
        val commune = communeService.findByIdCommune(request.service.communeId)
        val quartier =  quartierService.findByIdQuartier(request.service.quartierId)
        val city = cityService.findByIdCity(request.service.cityId)
        val user = userService.findIdUser(service.userId)
        val devise = deviseService.getById(service.deviseId)
        if (realisation.isNotEmpty()){
            val entity = ServiceSalubriteEntity(
                user = user!!.toEntityToDto(),
                devise = devise.toEntity(),
                experience = service.experience,
                description = service.description,
                address = service.address,
                communeValue = service.communeValue,
                quartierValue = service.quartierValue,
                cityValue = service.cityValue,
                countryValue = service.countryValue,
                minPrice = service.minPrice,
                maxPrice = service.maxPrice,
                city = city?.toEntity(),
                commune = commune?.toEntity(),
                quartier = quartier?.toEntity(),
            ).toDomain()
            val data = serviceAction.create(entity)
            realisation.map {
                realisationImage.create(
                    SalubriteRealisation(
                        service = data, name = it.image
                    ),
                    server = baseUrl
                )
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

    @Operation(summary = "Voir les Salubrités service")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllSalubrite(): ResponseEntity<Map<String, List<SalubriteTask>>> {
        val response = mapOf("salubrites" to serviceAction.getAllData())
        return ResponseEntity.ok().body(response)
    }
}