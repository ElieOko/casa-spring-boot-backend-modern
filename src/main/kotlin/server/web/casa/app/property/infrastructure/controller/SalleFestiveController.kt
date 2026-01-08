package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
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
import server.web.casa.utils.ApiResponse
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
    private val quartierService: QuartierService,
    private val propertyTypeService: PropertyTypeService
) {

    @Operation(summary = "Création salle festive")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFestive(
        @Valid @RequestBody request: SalleFestiveRequest,
    ) = coroutineScope{
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
    }

    @Operation(summary = "Listes des salles festives")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFestive() = coroutineScope {
        val data = service.getAll()
        val response = mapOf("festives" to data)
        ResponseEntity.ok().body(response)
    }

    @Operation(summary = "Get Salle Festive by User")
    @GetMapping("/owner/{userId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFestiveByUser(
        @PathVariable("userId") userId : Long,
    ) = coroutineScope {
        val data = service.getAllPropertyByUser(userId)
        ApiResponse(data)
    }
    @Operation(summary = "Modification Salle Festive image")
    @PutMapping("/image/{festiveId}")
    suspend fun updateFileFestive(
        @PathVariable("festiveId") festiveId : Long,
        @Valid @RequestBody request: ImageChange
    ) = coroutineScope {
        service.findById(festiveId)
        val result = if (request.images.isNotEmpty()) imageService.updateFile(festiveId,request.images) else false
        val message = mutableMapOf("message" to "Modification effectuée avec succès")
        if (result)  ResponseEntity.ok(message) else {
            message["message"] = "Aucune modification n'a été effectuée"
            ResponseEntity.badRequest().body(message)
        }
    }
    @Operation(summary = "Suppression Salle Festive image")
    @DeleteMapping("/image/{festiveId}")
    suspend fun deleteFile(
        @PathVariable("festiveId") festiveId : Long,
        @Valid @RequestBody request: PropertyImagesRequest
    ) = coroutineScope{
        service.findById(festiveId)
        val result = if (request.propertyImage.isNotEmpty()) imageService.deleteFile(festiveId,request.propertyImage) else false
        val message = mutableMapOf("message" to "Suppression effectuée avec succès")
        if (result)  ResponseEntity.ok(message) else {
            message["message"] = "Aucune suppression n'a été effectuée"
            ResponseEntity.badRequest().body(message)
        }
    }

    @Operation(summary = "Modification Salle Festive")
    @PutMapping("/owner/{festiveId}")
    suspend fun updateBureau(
        @PathVariable("festiveId") festiveId : Long,
        @Valid @RequestBody request: SalleFestiveDTO
    ) = coroutineScope {
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
        ResponseEntity.ok(message)
    }

    @Operation(summary = "Sold")
    @PutMapping("/sold/{propertyId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun soldOutOrInFestive(
        @PathVariable("propertyId") propertyId : Long,
        @RequestBody request : StatusState
    )= coroutineScope{
        val message = mutableMapOf("message" to if(request.status) "Proprièté bouqué(soldout) avec succès" else "Proprièté non bouqué(soldin) avec succès")
        val data = service.findById(propertyId)
        data.sold = request.status
        service.createOrUpdate(data)
        ResponseEntity.badRequest().body(message)
    }

    @Operation(summary = "Enable or disable")
    @PutMapping("/enable/{propertyId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun toEnableOrDisableFestive(
        @PathVariable("propertyId") propertyId : Long,
        @RequestBody request : StatusState
    )= coroutineScope{
        val message = mutableMapOf("message" to if(request.status) "Proprièté activé avec succès" else "Proprièté desactivé avec succès")
        val data= service.findById(propertyId)
        data.isAvailable = request.status
        service.createOrUpdate(data)
        ResponseEntity.badRequest().body(message)
    }
}