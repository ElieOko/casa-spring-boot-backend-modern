package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.property.application.service.AgenceService
import server.web.casa.app.property.application.service.VacanceService
import server.web.casa.app.property.domain.model.Agence
import server.web.casa.app.property.domain.model.request.VacanceRequest
import server.web.casa.app.property.domain.model.request.toDomain
import server.web.casa.route.property.PropertyRoute.PROPERTY_VACANCE
import server.web.casa.route.utils.AgenceRoute.AGENCE
import server.web.casa.utils.ApiResponse
import server.web.casa.utils.ApiResponseWithMessage

@Tag(name = "Vacance", description = "")
@RestController
@RequestMapping(PROPERTY_VACANCE )
class VacanceController(
    private val service: VacanceService,
    private val agenceService: AgenceService,
) {
    @Operation(summary = "Création vacance")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createVacance(
        @Valid @RequestBody request: VacanceRequest,
    )= coroutineScope {
        if (request.userId == null) ResponseEntity.badRequest().body("User ID must not be null!")
        if (request.images.size < 3) ResponseEntity.badRequest().body("Vous devez fournir au minimun 3 images pour votre site touristique")
        val agence = agenceService.getAllByUser(request.userId!!)
        if (agence.isEmpty()) ResponseEntity.badRequest().body("Vous devez avoir une agence au minimum un pour poster un site touristique")
        request.agenceId = agence.first()?.id
        val data = service.create(request.toDomain())


    }

    @Operation(summary = "List des vacance")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllVacance() = coroutineScope {
    }
}