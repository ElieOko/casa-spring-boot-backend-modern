package server.web.casa.app.address.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import server.web.casa.app.address.application.service.CountryService
import server.web.casa.app.address.application.service.QuartierService
import server.web.casa.app.address.domain.model.Country
import server.web.casa.app.address.domain.model.Quartier
import server.web.casa.route.address.AddressRoute
import server.web.casa.utils.Mode

const val ROUTE_QUARTIERS = AddressRoute.QUARTIERS

@Tag(name = "Quartier", description = "Gestion des quartiers")
@RestController
@RequestMapping(ROUTE_QUARTIERS)
@Profile(Mode.DEV)
class QuartierController(
   private val service : QuartierService
) {
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllQuartier(): ResponseEntity<Map<String, List<Quartier>>> {
        val data = service.findAllQuartier()
        val response = mapOf("quartiers" to data)
        return ResponseEntity.ok().body(response)
    }
}