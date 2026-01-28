package server.web.casa.app.property.infrastructure.controller

import server.web.casa.route.GlobalRoute
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.toList
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import server.web.casa.app.property.application.service.PropertyTypeService
import server.web.casa.app.property.domain.model.PropertyType
import server.web.casa.route.property.PropertyTypeScope
import server.web.casa.utils.ApiResponse

@Tag(name = "Property Type", description = "Gestion types propriètés")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
class PropertyTypeController(
    private val service : PropertyTypeService
) {
//    @PostMapping("/${PropertyTypeScope.PROTECTED}",consumes = [MediaType.APPLICATION_JSON_VALUE])
//    suspend fun createType(){
//    }

    @GetMapping("/${PropertyTypeScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllType(): ApiResponse<List<PropertyType>> {
        val data = service.getAll().toList()
        return ApiResponse(data)
    }
}