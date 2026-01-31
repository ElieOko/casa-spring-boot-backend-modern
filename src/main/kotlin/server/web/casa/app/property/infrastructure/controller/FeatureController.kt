package server.web.casa.app.property.infrastructure.controller

import server.web.casa.route.GlobalRoute
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.toList
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import server.web.casa.app.property.application.service.FeatureService
import server.web.casa.app.property.domain.model.Feature
import server.web.casa.route.property.PropertyFeatureScope
import server.web.casa.utils.ApiResponse
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Features", description = "Gestion des équipements")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
class FeatureController(
    private val service: FeatureService,
    private val sentry: SentryService,
) {

//    @PostMapping("/${PropertyFeatureScope.PROTECTED}",consumes = [MediaType.APPLICATION_JSON_VALUE])
//    fun createFeature(){
//
//    }

    @GetMapping("/${PropertyFeatureScope.PRIVATE}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFeature(request: HttpServletRequest): ApiResponse<List<Feature>> {
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
                    countName = "api.feature.getallfeature.count",
                    distributionName = "api.feature.getallfeature.latency"
                )
            )
        }
    }
}