package server.web.casa.app.prestation.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.prestation.application.*
import server.web.casa.app.prestation.domain.model.SollicitationDTO
import server.web.casa.app.prestation.domain.request.CotationRequest
import server.web.casa.app.prestation.infrastructure.persistance.entity.CotationPrestationEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.sollicitation.CotationScope
import server.web.casa.utils.Mode
import java.time.LocalDateTime
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Cotation", description = "cotation de prestation")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)
class CotationPrestationController(
    private val service: CotationPrestationService,
    private  val userS: UserService,
    private  val solS: SollicitationService,
    private val sentry: SentryService,
) {
    @PostMapping("/{version}/${CotationScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createCotation(
        request: HttpServletRequest,
        @Valid @RequestBody req: CotationRequest
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val checkUser = userS.findIdUser(req.userId)
            val checkSollicitation: SollicitationDTO? = (solS.findById(req.sollicitationId) ?: ResponseEntity.badRequest().body(mapOf("error" to "prestation not found"))) as SollicitationDTO?
            val prestateur = userS.findIdUser(checkSollicitation!!.user.userId!!)
            if (req.cote >= 6 ) ResponseEntity.badRequest().body(mapOf("error" to "Cote doit etre inferieure ou egale à 5"))
            val data = CotationPrestationEntity(userId = checkUser.userId!! , sollicitationId = checkSollicitation.sollicitation.id!!, cote = req.cote, commentaire = req.commentaire, isActive = true, createdAt = LocalDateTime.now())
            val created = service.create(data)
            ResponseEntity.ok(mapOf("cotation" to created, "prestateur" to prestateur, "evaluateur" to checkUser))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.cotationprestation.createcotation.count",
                    distributionName = "api.cotationprestation.createcotation.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${CotationScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllCotations(request: HttpServletRequest): ResponseEntity<Map<String, List<CotationPrestationEntity>>> =  coroutineScope{
        val startNanos = System.nanoTime()
        try {
            ResponseEntity.ok().body(mapOf("cotation" to service.findAll().toList()))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.cotationprestation.getallcotations.count",
                    distributionName = "api.cotationprestation.getallcotations.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${CotationScope.PUBLIC}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getCotationById(request: HttpServletRequest, @PathVariable id: Long) : ResponseEntity<Map<String, CotationPrestationEntity?>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = service.findById(id)
            val response = mapOf("cotation" to data)
            ResponseEntity.ok(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.cotationprestation.getcotationbyid.count",
                    distributionName = "api.cotationprestation.getcotationbyid.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${CotationScope.PROTECTED}/delete/{id}")
    suspend fun deleteById(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<Map<String, String>> = coroutineScope{
        val startNanos = System.nanoTime()
        try {
            service.findById(id) ?: ResponseEntity.ok(mapOf("message" to "cotation not found"))
            if(service.deleteById(id)) {
                ResponseEntity.ok(mapOf("message" to "cotation deleted successfully"))
            }else{ ResponseEntity.ok(mapOf("error" to "Something was wrong"))}
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.cotationprestation.deletebyid.count",
                    distributionName = "api.cotationprestation.deletebyid.latency"
                )
            )
        }
    }
}