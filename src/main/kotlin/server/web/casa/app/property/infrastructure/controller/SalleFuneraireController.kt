package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.address.application.service.*
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.property.application.service.*
import server.web.casa.app.property.domain.model.*
import server.web.casa.app.property.domain.model.request.ImageChange
import server.web.casa.app.property.domain.model.request.PropertyImageChangeRequest
import server.web.casa.app.property.domain.model.request.PropertyImagesRequest
import server.web.casa.app.property.domain.model.toDomain
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.property.PropertyRoute
import server.web.casa.utils.*

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
    private val quartierService: QuartierService,
    private val propertyTypeService: PropertyTypeService
) {

    @Operation(summary = "Création bureau")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFuneraire(
        @Valid @RequestBody request: SalleFuneraireRequest,
    ) = coroutineScope{
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
    }

    @Operation(summary = "List des bureaux")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFuneraire() = coroutineScope {
        val data = service.getAll()
        val response = mapOf("funeraires" to data)
        ResponseEntity.ok().body(response)
    }

    @Operation(summary = "Get Salle Funeraire by User")
    @GetMapping("/owner/{userId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFuneraireByUser(
        @PathVariable("userId") userId : Long,
    ) = coroutineScope {
        val data = service.getAllPropertyByUser(userId)
        ApiResponse(data)
    }
    @Operation(summary = "Modification Salle festive")
    @PutMapping("/image/{funeraireId}")
    suspend fun updateFile(
        @PathVariable("funeraireId") funeraireId : Long,
        @Valid @RequestBody request: ImageChange
    ) = coroutineScope {
        service.findById(funeraireId)
        val result = if (request.images.isNotEmpty()) imageService.updateFile(funeraireId,request.images) else false
        val message = mutableMapOf("message" to "Modification effectuée avec succès")
        if (result)  ResponseEntity.ok(message) else {
            message["message"] = "Aucune modification n'a été effectuée"
            ResponseEntity.badRequest().body(message)
        }
    }

    @Operation(summary = "Suppression Salle festive")
    @DeleteMapping("/image/{funeraireId}")
    suspend fun deleteFile(
        @PathVariable("funeraireId") funeraireId : Long,
        @Valid @RequestBody request: PropertyImagesRequest
    ) = coroutineScope{
        service.findById(funeraireId)
        val result = if (request.propertyImage.isNotEmpty()) imageService.deleteFile(funeraireId,request.propertyImage) else false
        val message = mutableMapOf("message" to "Suppression effectuée avec succès")
        if (result)  ResponseEntity.ok(message) else {
            message["message"] = "Aucune suppression n'a été effectuée"
            ResponseEntity.badRequest().body(message)
        }
    }

    @Operation(summary = "Modification Salle Funeraire")
    @PutMapping("/owner/{funeraireId}")
    suspend fun updateFuneraire(
        @PathVariable("funeraireId") funeraireId : Long,
        @Valid @RequestBody request: SalleFuneraireDTO
    ) = coroutineScope {
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
        ResponseEntity.ok(message)
    }
}