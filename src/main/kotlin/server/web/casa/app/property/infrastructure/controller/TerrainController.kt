package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
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
import server.web.casa.app.property.application.service.PropertyTypeService
import server.web.casa.app.property.application.service.TerrainImageService
import server.web.casa.app.property.application.service.TerrainService
import server.web.casa.app.property.domain.model.BureauDtoRequest
import server.web.casa.app.property.domain.model.BureauRequest
import server.web.casa.app.property.domain.model.ImageRequestStandard
import server.web.casa.app.property.domain.model.StatusState
import server.web.casa.app.property.domain.model.request.TerrainRequest
import server.web.casa.app.property.domain.model.request.toDomain
import server.web.casa.app.property.domain.model.toDomain
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.property.PropertyRoute
import server.web.casa.utils.ApiResponse
import server.web.casa.utils.ApiResponseWithMessage

@Tag(name = "Terrain", description = "")
@RestController
@RequestMapping(PropertyRoute.PROPERTY_TERRAIN)
class TerrainController(
    private val service : TerrainService,
    private val devise : DeviseService,
    private val userService: UserService,
    private val imageTerrain: TerrainImageService,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService,
    private val propertyTypeService: PropertyTypeService,
) {
    @Operation(summary = "Création Terrain")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createTerrain(
        @Valid @RequestBody request: TerrainRequest,
    ) = coroutineScope{
//        propertyTypeService.findByIdPropertyType(request.bureau.propertyTypeId?:0)
        if (request.propertyTypeId != 9L) throw ResponseStatusException(HttpStatusCode.valueOf(404), "Ce type n'appartient pas au Terrain")
        val city = if (request.cityId != null) cityService.findByIdCity(request.cityId) else null
        val commune = communeService.findByIdCommune(request.communeId)
        val quartier =  if (request.quartierId != null) quartierService.findByIdQuartier(request.quartierId) else null
        devise.getById(request.deviseId)
        userService.findIdUser(request.userId)
        request.cityId =  city?.cityId
        request.communeId = commune?.communeId
        request.quartierId = quartier?.quartierId
        if (request.images.isEmpty()) throw ResponseStatusException(HttpStatusCode.valueOf(404), "Precisez des images.")
        val data = service.create(request.toDomain())
        request.images.forEach { imageTerrain.create(ImageRequestStandard(data.id!!,it.image)) }
        ApiResponseWithMessage(
            data = data,
            message = "Enregistrement réussie pour la proprièté terrain",
        )
    }

    @Operation(summary = "List des terrain")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllTerrain() = coroutineScope {
        val data = service.getAll()
        val response = mapOf("terrain" to data)
        ResponseEntity.ok().body(response)
    }
    @Operation(summary = "Sold")
    @PutMapping("/sold/{propertyId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun soldOutOrInTerrain(
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
    suspend fun toEnableOrDisableTerrain(
        @PathVariable("propertyId") propertyId : Long,
        @RequestBody request : StatusState
    )= coroutineScope{
        val message = mutableMapOf("message" to if(request.status) "Proprièté activé avec succès" else "Proprièté desactivé avec succès")
        val data= service.findById(propertyId)
        data.isAvailable = request.status
        service.createOrUpdate(data)
        ResponseEntity.badRequest().body(message)
    }
    @Operation(summary = "Get Terrain by User")
    @GetMapping("/owner/{userId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllTerrainByUser(
        @PathVariable("userId") userId : Long,
    ) = coroutineScope {
        val data = service.getAllPropertyByUser(userId)
        ApiResponse(data)
    }

    @Operation(summary = "Modification Terrain")
    @PutMapping("/owner/{terrainId}")
    suspend fun updateTerrain(
        @PathVariable("terrainId") terrainId : Long,
        @Valid @RequestBody request: TerrainRequest
    ) = coroutineScope {
        userService.findIdUser(request.userId)
        val terrain = service.findById(terrainId)
        if (terrain.userId != request.userId) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet utilisateur n'appartient pas à la proprièté terrain.")
        val city = cityService.findByIdCity(request.cityId)
        val propertyType = propertyTypeService.findByIdPropertyType(request.propertyTypeId!!)
        val commune = communeService.findByIdCommune(request.communeId)
        val quartier =  quartierService.findByIdQuartier(request.quartierId)
        val data = request.toDomain()
        data.cityId = city?.cityId
        data.propertyTypeId = propertyType.propertyTypeId
        data.id = terrainId
        data.communeId = commune?.communeId
        data.quartierId = quartier?.quartierId
        service.update(data)
        val message = mutableMapOf("message" to "Modification effectuée avec succès")
        ResponseEntity.ok(message)
    }
}