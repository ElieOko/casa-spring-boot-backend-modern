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
import server.web.casa.app.property.application.service.FestiveImageService
import server.web.casa.app.property.application.service.SalleFestiveService
import server.web.casa.app.property.domain.model.Bureau
import server.web.casa.app.property.domain.model.BureauDto
import server.web.casa.app.property.domain.model.ImageRequestStandard
import server.web.casa.app.property.domain.model.SalleFestiveRequest
import server.web.casa.app.property.domain.model.toDomain
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.property.PropertyRoute
import server.web.casa.utils.ApiResponseWithMessage

@Tag(name = "Festive", description = "")
@RestController
@RequestMapping(PropertyRoute.PROPERTY_FESTIVE)
class SalleFestiveController(
    private val service: SalleFestiveService,
    private val devise : DeviseService,
    private val userService: UserService,
    private val imageService: FestiveImageService,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService
) {

    @Operation(summary = "Création salle festive")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFestive(
        @Valid @RequestBody request: SalleFestiveRequest,
    ) = coroutineScope{
        val city = if (request.festive.cityId != null) cityService.findByIdCity(request.festive.cityId) else null
        val commune = communeService.findByIdCommune(request.festive.communeId)
        val quartier =  if (request.festive.quartierId != null) quartierService.findByIdQuartier(request.festive.quartierId) else null
        request.festive.cityId =  city?.cityId
        request.festive.communeId = commune?.communeId
        request.festive.quartierId = quartier?.quartierId
        devise.getById(request.festive.deviseId)
        userService.findIdUser(request.festive.userId!!)
        if (request.images.isEmpty()) throw ResponseStatusException(HttpStatusCode.valueOf(404), "Precisez des images.")
        val data = service.create(request.festive.toDomain())
        request.images.forEach { imageService.create(ImageRequestStandard(data.id!!,it.image)) }
        ApiResponseWithMessage(data = data, message = "Enregistrement réussie pour la proprièté festive")
    }

    @Operation(summary = "Listes des salles festives")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFestive() = coroutineScope {
        val data = service.getAll()
        val response = mapOf("festives" to data)
        ResponseEntity.ok().body(response)
    }
}