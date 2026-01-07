package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import server.web.casa.app.property.application.service.AgenceService
import server.web.casa.app.property.domain.model.Agence
import server.web.casa.route.utils.AgenceRoute.AGENCE
import server.web.casa.utils.ApiResponse
import server.web.casa.utils.ApiResponseWithMessage

@Tag(name = "Agence", description = "")
@RestController
@RequestMapping(AGENCE)
class AgenceController(
    private val service: AgenceService
) {
    @Operation(summary = "Création agence")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createAgence(
        @Valid @RequestBody request: Agence,
    ): ApiResponseWithMessage<Agence> = coroutineScope {
        val result = service.create(request)
        ApiResponseWithMessage(message = "Enregistrement réussie pour votre agence ${result.name}", data = result)
    }

    @Operation(summary = "List des agences")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllAgence(): ApiResponse<List<Agence>> = coroutineScope {
        val data = service.getAllAgence()
        ApiResponse(data)
    }

    @Operation(summary = "List des agences")
    @GetMapping("/owner/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllAgenceByUser(
        @PathVariable("userId") userId : Long,
    )= coroutineScope {
//        val ownerId = SecurityContextHolder.getContext().authentication!!.principal as String
        val data = service.getAllByUser(userId)
        ApiResponse(data)
    }
}