package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import server.web.casa.app.property.application.service.HotelService
import server.web.casa.app.property.domain.model.Hotel
import server.web.casa.route.property.PropertyFeatures.PROPERTY_HOTEL_PATH
import server.web.casa.utils.ApiResponse
import server.web.casa.utils.ApiResponseWithMessage

@Tag(name = "Hotel", description = "")
@RestController
@RequestMapping(PROPERTY_HOTEL_PATH)
class HotelController(
    private val service: HotelService,

) {
    @Operation(summary = "Création Hotel")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createHotel(
        @Valid @RequestBody request: Hotel,
    ) = coroutineScope {
        val result = service.save(request)
        ApiResponseWithMessage(message = "Enregistrement réussie pour votre hotel ${result.title}", data = result)
    }

    @Operation(summary = "List des hotels")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllHotel() = coroutineScope {
        val data = service.getAllHotel()
        ApiResponse(data)
    }

    @Operation(summary = "List des hotels")
    @GetMapping("/owner/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllHotelByUser(
        @PathVariable("userId") userId : Long,
    )= coroutineScope {
        val data = service.getAllByUser(userId)
        ApiResponse(data)
    }
}