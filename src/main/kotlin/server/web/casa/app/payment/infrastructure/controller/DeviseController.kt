package server.web.casa.app.payment.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.coroutineScope
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.route.utils.AgenceFeature.DEVISE

@RestController
@RequestMapping("api")
@Profile("dev")
class DeviseController(
    private val service: DeviseService,
) {
    @Operation(summary = "Liste des devises")
    @GetMapping("/{version}/public/devises",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllAccountE(): Map<String, List<Devise>>  = coroutineScope {
        val data = service.getAllData().toList()
        mapOf("devises" to data)
    }
}