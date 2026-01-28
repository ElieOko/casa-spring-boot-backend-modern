package server.web.casa.app.address.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.address.application.service.*
import server.web.casa.app.address.domain.model.*
import server.web.casa.app.address.domain.model.request.*
import server.web.casa.route.address.DistrictScope
import server.web.casa.utils.Mode

@Tag(name = "District", description = "Gestion des districts")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)
class DistrictController(
   private val service : DistrictService,
   private val cityService: CityService,
) {
    @PostMapping("/{version}/${DistrictScope.PROTECTED}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createDistrict(
       @Valid @RequestBody request: DistrictRequest
    ): ResponseEntity<out Map<String, Any?>> {
        val city = cityService.findByIdCity(request.cityId)
        if (city != null){
            val data = District(
                city = city.cityId ,
                name = request.name
            )
          val result = service.saveDistrict(data)
          val response = mapOf(
              "district" to result,
              "message" to "Enregistrement réussie avec succès"
          )
            return ResponseEntity.status(201).body(response)
        }
        val response = mapOf(
            "message" to "cette ville est inexistante !!!"
        )
        return ResponseEntity.badRequest().body(response)
    }

    @GetMapping("/{version}/${DistrictScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllDistrict(): ResponseEntity<Map<String, List<District?>>> {
        val data = service.findAllDistrict()
        val response = mapOf("districts" to data)
        return ResponseEntity.ok().body(response)
    }
}