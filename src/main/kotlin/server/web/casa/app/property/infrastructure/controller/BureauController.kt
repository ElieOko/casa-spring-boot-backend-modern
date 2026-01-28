package server.web.casa.app.property.infrastructure.controller

import server.web.casa.route.GlobalRoute
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.address.application.service.*
import server.web.casa.app.payment.application.service.*
import server.web.casa.app.property.application.service.*
import server.web.casa.app.property.domain.model.*
import server.web.casa.app.property.domain.model.filter.PropertyFilter
import server.web.casa.app.property.domain.model.request.*
import server.web.casa.app.property.domain.model.toDomain
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.property.PropertyBureauScope
import server.web.casa.utils.*

@Tag(name = "Bureau", description = "")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
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
    @PostMapping("/${PropertyBureauScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createBureau(
        @Valid @RequestBody request: BureauDtoRequest,
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
    @GetMapping("/${PropertyBureauScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllBureau() = coroutineScope {
        val data = service.getAllBureau()
        val response = mapOf("bureaux" to data)
        ResponseEntity.ok().body(response)
    }

    @Operation(summary = "Modification Bureau")
    @PutMapping("/${PropertyBureauScope.PROTECTED}/image/{bureauId}")
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
    @DeleteMapping("/${PropertyBureauScope.PROTECTED}/image/{bureauId}")
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
    @GetMapping("/${PropertyBureauScope.PROTECTED}/owner/{userId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllBureauByUser(
        @PathVariable("userId") userId : Long,
    ) = coroutineScope {
        val data = service.getAllPropertyByUser(userId)
        ApiResponse(data)
    }

    @Operation(summary = "Modification Bureau")
    @PutMapping("/${PropertyBureauScope.PROTECTED}/owner/{bureauId}")
    suspend fun updateBureau(
        @PathVariable("bureauId") bureauId : Long,
        @Valid @RequestBody request: BureauRequest
    ) = coroutineScope {
        userService.findIdUser(request.userId!!)
        val bureau = service.findById(bureauId)
        if (bureau.userId != request.userId) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet utilisateur n'appartient pas à la proprièté bureau.")
        val city = cityService.findByIdCity(request.cityId)
        val propertyType = propertyTypeService.findByIdPropertyType(request.propertyTypeId!!)
        val commune = communeService.findByIdCommune(request.communeId)
        val quartier =  quartierService.findByIdQuartier(request.quartierId)
        val data = request.toDomain()
        data.cityId = city?.cityId
        data.propertyTypeId = propertyType.propertyTypeId
        data.id = bureauId
        data.communeId = commune?.communeId
        data.quartierId = quartier?.quartierId
        service.update(data)
        val message = mutableMapOf("message" to "Modification effectuée avec succès")
        ResponseEntity.ok(message)
    }

    @GetMapping("/${PropertyBureauScope.PUBLIC}/filter",produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Get property by sold into price, room, city, commune with pagination and sorting")
    suspend fun getAllBureauFilter(
        @Parameter(description = "Transaction Type") @RequestParam transactionType : String,
        @Parameter(description = "Prix minimun") @RequestParam minPrice : Double,
        @Parameter(description = "Prix maximun") @RequestParam maxPrice : Double,
        @Parameter(description = "Type de maison") @RequestParam typeMaison : Long,
        @Parameter(description = "Ville") @RequestParam city : Long?,
        @Parameter(description = "Commune") @RequestParam commune : Long?,
        @Parameter(description = "Ville value") @RequestParam cityValue : String?,
        @Parameter(description = "Commune value") @RequestParam communeValue : String?,
        @Parameter(description = "Chambre") @RequestParam room : Int,
        @Parameter(description = "Page number(0-based)") @RequestParam(defaultValue = "0") page : Int,
        @Parameter(description = "Page size") @RequestParam(defaultValue = "20") size : Int,
        @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") sortBy : String,
        @Parameter(description = "Sort order (asc/desc)") @RequestParam(defaultValue = "asc") sortOrder : String
    ) = coroutineScope {
        val data = service.filter(
            filterModel = PropertyFilter(
                transactionType = transactionType,
                minPrice = minPrice,
                maxPrice = maxPrice,
                city = city,
                commune = commune,
                typeMaison = typeMaison,
                room = room,
                cityValue = cityValue,
                communeValue = communeValue,
            ),
            page = page,
            size = size,
            sortBy = sortBy,
            sortOrder = sortOrder
        )
        val response = mapOf("properties" to data)
        ResponseEntity.ok().body(response)
    }

    @Operation(summary = "Sold")
    @PutMapping("/${PropertyBureauScope.PROTECTED}/sold/{propertyId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun soldOutOrInBureau(
        @PathVariable("propertyId") propertyId : Long,
        @RequestBody request : StatusState
    ) = coroutineScope{
        val message = mutableMapOf("message" to if(request.status) "Proprièté bouqué(soldout) avec succès" else "Proprièté non bouqué(soldin) avec succès")
        val data = service.findById(propertyId)
        data.sold = request.status
        service.createOrUpdate(data)
        ResponseEntity.badRequest().body(message)
    }

    @Operation(summary = "Enable or disable")
    @PutMapping("/${PropertyBureauScope.PROTECTED}/enable/{propertyId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun toEnableOrDisableBureau(
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