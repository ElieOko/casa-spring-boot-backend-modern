package server.web.casa.app.ecosystem.infrastructure.controller.other

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.address.application.service.CityService
import server.web.casa.app.address.application.service.CommuneService
import server.web.casa.app.address.application.service.QuartierService
import server.web.casa.app.address.infrastructure.persistence.mapper.toEntity
import server.web.casa.app.ecosystem.application.service.task.AjusteurServiceAction
import server.web.casa.app.ecosystem.application.service.task.PeintreServiceAction
import server.web.casa.app.ecosystem.domain.model.realisation.FrigoristeRealisation
import server.web.casa.app.ecosystem.domain.model.realisation.PeintreRealisation
import server.web.casa.app.ecosystem.domain.model.task.PeintreTask
import server.web.casa.app.ecosystem.domain.model.task.PlombierTask
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.frigoriste.ServiceFrigoristeEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.frigoriste.toDomain
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.peintre.ServicePeintreEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.peintre.toDomain
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.payment.domain.model.toEntity
import server.web.casa.app.property.domain.model.request.PropertyRequest
import server.web.casa.app.user.application.service.UserService
import server.web.casa.app.user.infrastructure.persistence.mapper.toEntityToDto
import server.web.casa.route.ecosystem.Service

@Tag(name = "Peintre Service", description = "Gestion des services Peintre")
@RestController
@RequestMapping(Service.PEINTRE)
class PeintreServiceController(
    private val userService: UserService,
    private val deviseService: DeviseService,
    private val realisationImage : RealisationPeintreServiceAction,
    private val serviceAction : PeintreServiceAction,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createPeintreService(
        @Valid @RequestBody request: PeintreServiceRequest,
        requestHttp: HttpServletRequest
    ): ResponseEntity<Map<String, String>> {
        val commune = communeService.findByIdCommune(request.service.communeId)
        val quartier =  quartierService.findByIdQuartier(request.service.quartierId)
        val city = cityService.findByIdCity(request.service.cityId)
        val isProd = true
        val baseUrl = if (isProd) "${requestHttp.scheme}://${requestHttp.serverName}"  else  "${requestHttp.scheme}://${requestHttp.serverName}:${requestHttp.serverPort}"
        val realisation = request.realisation
        val service = request.service
        val user = userService.findIdUser(service.userId)
        val devise = deviseService.getById(service.deviseId)
        if (realisation.isNotEmpty()){
            val entity = ServicePeintreEntity(
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
                    PeintreRealisation(
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
    @Operation(summary = "Voir les peintres service")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPeintre(): ResponseEntity<Map<String, List<PeintreTask>>> {
        val response = mapOf("peintres" to serviceAction.getAllData())
        return ResponseEntity.ok().body(response)
    }
}