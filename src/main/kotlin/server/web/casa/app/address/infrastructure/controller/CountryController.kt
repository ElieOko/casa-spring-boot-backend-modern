package server.web.casa.app.address.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.address.application.service.CountryService
import server.web.casa.app.address.domain.model.Country
import server.web.casa.route.address.CountryScope
import server.web.casa.utils.Mode

@Tag(name = "Country", description = "Gestion des pays")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)
class CountryController(
   private val service : CountryService
) {
    @GetMapping("/{version}/${CountryScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllCountry(): ResponseEntity<Map<String, List<Country>>> {
        val data = service.findAllCountry()
        val response = mapOf("countries" to data)
        return ResponseEntity.ok().body(response)
    }
}