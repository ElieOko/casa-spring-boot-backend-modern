package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.address.application.service.CityService
import server.web.casa.app.address.application.service.CommuneService
import server.web.casa.app.address.application.service.QuartierService
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.property.application.service.BureauImageService
import server.web.casa.app.property.application.service.BureauService
import server.web.casa.app.property.application.service.FuneraireImageService
import server.web.casa.app.property.application.service.SalleFuneraireService
import server.web.casa.app.property.domain.model.Bureau
import server.web.casa.app.property.domain.model.BureauDto
import server.web.casa.app.property.domain.model.ImageRequestStandard
import server.web.casa.app.property.domain.model.SalleFuneraireRequest
import server.web.casa.app.property.domain.model.toDomain
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.property.PropertyRoute
import server.web.casa.utils.ApiResponseWithMessage

@Tag(name = "Funeraire", description = "")
@RestController
@RequestMapping(PropertyRoute.PROPERTY_FUNERAIRE)
class SalleFuneraireController(
    private val service: SalleFuneraireService,
    private val devise : DeviseService,
    private val userService: UserService,
    private val imageService: FuneraireImageService,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService
) {

    @Operation(summary = "Création bureau")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFuneraire(
        @Valid @RequestBody request: SalleFuneraireRequest,
    ) = coroutineScope{
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
    }

    @Operation(summary = "List des bureaux")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFuneraire() = coroutineScope {
        val data = service.getAll()
        val response = mapOf("funeraires" to data)
        ResponseEntity.ok().body(response)
    }
}