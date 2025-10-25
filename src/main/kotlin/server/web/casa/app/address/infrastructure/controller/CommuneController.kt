package server.web.casa.app.address.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.address.application.service.CommuneService
import server.web.casa.app.address.application.service.DistrictService
import server.web.casa.app.address.domain.model.Commune
import server.web.casa.app.address.domain.model.request.CommuneRequest
import server.web.casa.route.address.AddressRoute
import server.web.casa.utils.Mode

const val ROUTE_COMMUNE = AddressRoute.COMMUNES

@Tag(name = "Commune", description = "Gestion des communes")
@RestController
@RequestMapping(ROUTE_COMMUNE)
@Profile(Mode.DEV)
class CommuneController(
   private val service : CommuneService,
   private val districtService: DistrictService,
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createCommune(
       @Valid @RequestBody request: CommuneRequest
    ): ResponseEntity<Map<String, Any>> {
        val district = districtService.findByIdDistrict(request.districtId)
        if (district != null){
            val data = Commune(
                district = district ,
                name = request.name
            )
          val result = service.saveCommune(data)
          val response = mapOf(
              "commune" to result,
              "message" to "Enregistrement réussie avec succès"
          )
            return ResponseEntity.status(201).body(response)
        }
        val response = mapOf(
            "message" to "cet district est inexistant !!!"
        )
        return ResponseEntity.badRequest().body(response)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllCommune(): ResponseEntity<Map<String, List<Commune>>> {
        val data = service.findAllCommune()
        val response = mapOf("communes" to data)
        return ResponseEntity.ok().body(response)
    }
}