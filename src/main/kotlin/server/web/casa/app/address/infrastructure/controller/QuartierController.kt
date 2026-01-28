package server.web.casa.app.address.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.address.application.service.QuartierService
import server.web.casa.app.address.domain.model.Quartier
import server.web.casa.route.address.QuartierScope
import server.web.casa.utils.Mode

@Tag(name = "Quartier", description = "Gestion des quartiers")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)
class QuartierController(
   private val service : QuartierService
) {
    @GetMapping("/{version}/${QuartierScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllQuartier(): ResponseEntity<Map<String, List<Quartier>>> {
        val data = service.findAllQuartier()
        val response = mapOf("quartiers" to data)
        return ResponseEntity.ok().body(response)
    }
}