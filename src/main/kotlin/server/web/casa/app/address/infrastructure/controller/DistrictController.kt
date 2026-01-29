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
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import kotlinx.coroutines.coroutineScope
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "District", description = "Gestion des districts")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)
class DistrictController(
   private val service : DistrictService,
   private val cityService: CityService,
   private val sentry: SentryService,
) {
    @PostMapping("/{version}/${DistrictScope.PROTECTED}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createDistrict(
       httpRequest: HttpServletRequest,
       @Valid @RequestBody request: DistrictRequest
    ): ResponseEntity<out Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
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
                return ResponseEntity.status(201).body(response)}
            val response = mapOf(
                "message" to "cette ville est inexistante !!!"
            )
            return ResponseEntity.badRequest().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.district.createdistrict.count",
                    distributionName = "api.district.createdistrict.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${DistrictScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllDistrict(request: HttpServletRequest) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = service.findAllDistrict()
            val response = mapOf("districts" to data)
            ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.district.getalldistrict.count",
                    distributionName = "api.district.getalldistrict.latency"
                )
            )
        }
    }
}