package server.web.casa.app.property.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.property.application.service.PropertyTypeService
import server.web.casa.app.property.domain.model.PropertyType
import server.web.casa.route.property.PropertyRoute
import server.web.casa.utils.ApiResponse

const val ROUTE_PROPERTY_TYPE = PropertyRoute.PROPERTY_TYPE

@Tag(name = "Property Type", description = "Gestion types propriètés")
@RestController
@RequestMapping(ROUTE_PROPERTY_TYPE)
class PropertyTypeController(
    private val service : PropertyTypeService
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createType(){
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllType(): ApiResponse<List<PropertyType>> {
        val data = service.getAll().toList()
        return ApiResponse(data)
    }
}