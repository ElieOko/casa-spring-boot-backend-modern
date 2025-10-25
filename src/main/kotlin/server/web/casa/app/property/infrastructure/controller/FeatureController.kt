package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.property.application.service.FeatureService
import server.web.casa.app.property.domain.model.Feature
import server.web.casa.route.property.PropertyRoute

const val ROUTE_PROPERTY_FEATURE = PropertyRoute.PROPERTY_FEATURE

@Tag(name = "Features", description = "Gestion des équipements")
@RestController
@RequestMapping(ROUTE_PROPERTY_FEATURE)
class FeatureController(
    private val service: FeatureService
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createFeature(){

    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFeature(): ResponseEntity<Map<String, List<Feature>>> {
        val data = service.getAll()
        val response = mapOf("features" to data)
        return ResponseEntity.ok().body(response)
    }
}