package server.web.casa.app.property.infrastructure.controller

import server.web.casa.route.GlobalRoute
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.property.application.service.*
import server.web.casa.app.property.domain.model.*
import server.web.casa.app.property.domain.model.request.*
import server.web.casa.route.property.PropertyVacanceScope
import server.web.casa.utils.*

@Tag(name = "Vacance", description = "")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
class VacanceController(
    private val service: VacanceService,
    private val agenceService: AgenceService,
    private val imageService: VacanceImageService
) {
    @Operation(summary = "Création vacance")
    @PostMapping("/${PropertyVacanceScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createVacance(
        @Valid @RequestBody request: VacanceRequest,
    )= coroutineScope {
        if (request.userId == null) ResponseEntity.badRequest().body("User ID must not be null!")
        if (request.images.size < 3) ResponseEntity.badRequest().body("Vous devez fournir au minimun 3 images pour votre site touristique")
        val agence = agenceService.getAllByUser(request.userId!!)
        if (agence.isEmpty()) ResponseEntity.badRequest().body("Vous devez avoir une agence au minimum un pour poster un site touristique")
        request.agenceId = agence.first().agence?.id
        val data = service.create(request.toDomain())
        request.images.forEach { imageService.create(ImageRequestStandard(data.id!!,it?.name!!)) }
        ApiResponseWithMessage(data = data, message = "Enregistrement réussie pour la votre cite de vacance",)
    }

    @Operation(summary = "List des vacance")
    @GetMapping("/${PropertyVacanceScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllVacance() = coroutineScope {
        ApiResponse(service.getAllVacance())
    }


}