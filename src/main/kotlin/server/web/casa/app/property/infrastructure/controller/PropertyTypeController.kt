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
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Property Type", description = "Gestion types propriètés")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
class PropertyTypeController(
    private val service : PropertyTypeService,
    private val sentry: SentryService,
) {
//    @PostMapping("/${PropertyTypeScope.PROTECTED}",consumes = [MediaType.APPLICATION_JSON_VALUE])
//    suspend fun createType(){
//    }

    @GetMapping("/${PropertyTypeScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllType(request: HttpServletRequest): ApiResponse<List<PropertyType>> {
        val startNanos = System.nanoTime()
        try {
            val data = service.getAll().toList()
            return ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.propertytype.getalltype.count",
                    distributionName = "api.propertytype.getalltype.latency"
                )
            )
        }
    }
}