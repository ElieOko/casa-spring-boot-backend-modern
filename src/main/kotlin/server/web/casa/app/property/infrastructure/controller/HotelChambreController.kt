package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.property.application.service.*
import server.web.casa.app.property.domain.model.*
import server.web.casa.app.property.domain.model.toDomain
import server.web.casa.route.property.PropertyRoute.PROPERTY_HOTEL_CHAMBRE
import server.web.casa.utils.ApiResponse
import server.web.casa.utils.ApiResponseWithMessage

@Tag(name = "Hotel Chambre", description = "")
@RestController
@RequestMapping(PROPERTY_HOTEL_CHAMBRE )
class HotelChambreController(
    private val service: HotelChambreService,
    private val hotel: HotelService,
    private val imageService: HotelChambreImageService
) {
    @Operation(summary = "Création Hotel Chambre")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createHotelChambre(
        @Valid @RequestBody request: HotelChambreRequest,
    ) = coroutineScope {
        if (request.userId == null) ResponseEntity.badRequest().body("User ID must not be null!")
        if (request.images.size < 3) ResponseEntity.badRequest().body("Vous devez fournir au minimun 3 images pour votre chambres")
        val hotel = hotel.getAllByUser(request.userId!!)
        if (hotel.isEmpty()) ResponseEntity.badRequest().body("Vous devez avoir une hotel au minimum un pour poster un vos chambres")
        request.hotelId = hotel.first().hotel.id
        val data = service.create(request.toDomain())
        request.images.forEach { imageService.create(ImageRequestStandard(data.id!!,it.image)) }
        ApiResponseWithMessage(
            data = data,
            message = "Enregistrement réussie pour votre chambre",
        )
    }

    @Operation(summary = "List des chambres")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllHotelChambre() = coroutineScope {
        ApiResponse(service.getAll())
    }

}