package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.property.application.service.BureauImageService
import server.web.casa.app.property.application.service.BureauService
import server.web.casa.app.property.domain.model.Bureau
import server.web.casa.app.property.domain.model.BureauDto
import server.web.casa.app.property.domain.model.ImageRequestStandard
import server.web.casa.app.property.domain.model.toDomain
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.property.PropertyRoute
import server.web.casa.utils.ApiResponseWithMessage

@Tag(name = "Bureau", description = "")
@RestController
@RequestMapping(PropertyRoute.PROPERTY_BUREAU)
class BureauController(
    private val service: BureauService,
    private val devise : DeviseService,
    private val userService: UserService,
    private val bureauImageService: BureauImageService,
) {

    @Operation(summary = "Création bureau")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createBureau(
        @Valid @RequestBody request: BureauDto,
    ) = coroutineScope{
        devise.getById(request.bureau.deviseId)
        userService.findIdUser(request.bureau.userId!!)
        if (request.images.isEmpty()) throw ResponseStatusException(HttpStatusCode.valueOf(404), "Precisez des images.")
        val data = service.create(request.bureau.toDomain())
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
}