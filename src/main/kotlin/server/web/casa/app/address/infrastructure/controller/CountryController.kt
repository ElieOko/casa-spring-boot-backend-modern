package server.web.casa.app.address.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import server.web.casa.app.address.application.service.CountryService
import server.web.casa.app.address.domain.model.Country
import server.web.casa.route.address.AddressRoute
import server.web.casa.utils.Mode

const val ROUTE = AddressRoute.COUNTRIES

@Tag(name = "Country", description = "Gestion des pays")
@RestController
@RequestMapping(ROUTE)
@Profile(Mode.DEV)
class CountryController(
   private val service : CountryService
) {
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllCountry(): ResponseEntity<Map<String, List<Country>>> {
        val data = service.findAllCountry()
        val response = mapOf("countries" to data)
        return ResponseEntity.ok().body(response)
    }
}