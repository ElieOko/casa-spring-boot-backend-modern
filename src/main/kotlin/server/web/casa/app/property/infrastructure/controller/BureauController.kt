package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
import server.web.casa.app.property.application.service.PropertyTypeService
import server.web.casa.app.property.domain.model.BureauDto
import server.web.casa.app.property.domain.model.ImageRequestStandard
import server.web.casa.app.property.domain.model.request.ImageChange
import server.web.casa.app.property.domain.model.request.ImageChangeOther
import server.web.casa.app.property.domain.model.toDomain
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.property.PropertyRoute
import server.web.casa.utils.ApiResponse
import server.web.casa.utils.ApiResponseWithMessage

@Tag(name = "Bureau", description = "")
@RestController
@RequestMapping(PropertyRoute.PROPERTY_BUREAU)
class BureauController(
    private val service: BureauService,
    private val devise : DeviseService,
    private val userService: UserService,
    private val bureauImageService: BureauImageService,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService,
    private val propertyTypeService: PropertyTypeService,
) {

    @Operation(summary = "Création bureau")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createBureau(
        @Valid @RequestBody request: BureauDto,
    ) = coroutineScope{
//        propertyTypeService.findByIdPropertyType(request.bureau.propertyTypeId?:0)
        if (request.bureau.propertyTypeId != 4L) throw ResponseStatusException(HttpStatusCode.valueOf(404), "Ce type n'appartient pas au bureaux")
        val city = if (request.bureau.cityId != null) cityService.findByIdCity(request.bureau.cityId) else null
        val commune = communeService.findByIdCommune(request.bureau.communeId)
        val quartier =  if (request.bureau.quartierId != null) quartierService.findByIdQuartier(request.bureau.quartierId) else null
        devise.getById(request.bureau.deviseId)
        userService.findIdUser(request.bureau.userId!!)
        request.bureau.cityId =  city?.cityId
        request.bureau.communeId = commune?.communeId
        request.bureau.quartierId = quartier?.quartierId
        if (request.images.isEmpty()) throw ResponseStatusException(HttpStatusCode.valueOf(404), "Precisez des images.")
        val data = service.create(request.bureau.toDomain(),request.features)
        request.images.forEach { bureauImageService.create(ImageRequestStandard(data.id!!,it.image)) }
        ApiResponseWithMessage(
           data = data,
            message = "Enregistrement réussie pour la proprièté bureau",
        )
    }

    @Operation(summary = "List des bureaux")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllBureau() = coroutineScope {
        val data = service.getAllBureau()
        val response = mapOf("bureaux" to data)
        ResponseEntity.ok().body(response)
    }

    @Operation(summary = "Modification Bureau")
    @PutMapping("/image/{bureauId}")
    suspend fun updateFileBureau(
        @PathVariable("bureauId") bureauId : Long,
        @Valid @RequestBody request: ImageChange
    ) = coroutineScope {
        service.findById(bureauId)
        val result = if (request.images.isNotEmpty()) bureauImageService.updateFile(bureauId,request.images) else false
        val message = mutableMapOf("message" to "Modification effectuée avec succès")
        if (result)  ResponseEntity.ok(message) else {
            message["message"] = "Aucune modification n'a été effectuée"
            ResponseEntity.badRequest().body(message)
        }
    }

    @Operation(summary = "Suppression Bureau image")
    @DeleteMapping("/image/{bureauId}")
    suspend fun deleteFileBureau(
        @PathVariable("bureauId") bureauId : Long,
        @Valid @RequestBody request: ImageChangeOther
    ) = coroutineScope{
        service.findById(bureauId)
        val result = if (request.images.isNotEmpty()) bureauImageService.deleteFile(bureauId,request.images) else false
        val message = mutableMapOf("message" to "Suppression effectuée avec succès")
        if (result )  ResponseEntity.ok(message) else {
            message["message"] = "Aucune suppression n'a été effectuée"
            ResponseEntity.badRequest().body(message)
        }
    }
    @Operation(summary = "Get Bureau by User")
    @GetMapping("/owner/{userId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllBureauByUser(
        @PathVariable("userId") userId : Long,
    ) = coroutineScope {
        val data = service.getAllPropertyByUser(userId)
        ApiResponse(data)
    }
}