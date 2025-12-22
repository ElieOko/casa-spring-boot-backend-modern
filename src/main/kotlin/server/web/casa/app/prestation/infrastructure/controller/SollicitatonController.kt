package server.web.casa.app.prestation.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.ecosystem.application.service.PrestationService
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.prestation.application.SollicitationService
import server.web.casa.app.prestation.domain.request.SollicitationRequest
import server.web.casa.app.prestation.infrastructure.persistance.entity.SollicitationEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.sollicitation.SollicitationRoute
import server.web.casa.utils.Mode

const val ROUTE_SOLLICITATION = SollicitationRoute.SOLLICITATION

@Tag(name = "Sollicitation", description = "Sollicitation de prestation")
@RestController
@RequestMapping(ROUTE_SOLLICITATION)
@Profile(Mode.DEV)
class SollicitatonController(
    private val service: SollicitationService,
    private  val userS: UserService,
    private  val prestS: PrestationService,
    private  val deviseS: DeviseService
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createSollicitation(
        @Valid @RequestBody req: SollicitationRequest
    ):ResponseEntity <Map<String, Any?>>{
        val checkUser = userS.findIdUser(req.userId) ?: return ResponseEntity.badRequest().body(mapOf("error" to "user not found"))
        val checkPrestation = prestS.getById(req.prestationId) ?: return ResponseEntity.badRequest().body(mapOf("error" to "prestation not found"))
        val checkDevise = deviseS.getById(req.deviseId) ?: return ResponseEntity.badRequest().body(mapOf("error" to "devise not found"))
        val prestateur = userS.findIdUser(checkPrestation.userId)
        val data = SollicitationEntity(
            userId = checkUser.userId,
            prestationId = checkPrestation.id,
            deviseId = checkDevise.id,
            budegt = req.budegt,
            description = req.description,
            startDate = req.startDate,
            endDate = req.endDate,
        )
        val created = service.create(data)
        return ResponseEntity.ok(mapOf(
            "sollicitation" to created,
            "prestateur" to prestateur,
            "demandeur" to checkUser))
    }

    @GetMapping("/",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllSollicitations(): ResponseEntity<Map<String, List<SollicitationEntity>>>{
        return ResponseEntity.ok().body(
            mapOf(
                "sollicitation" to service.findAll().toList()
            ))
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getSollicitationById(@PathVariable id: Long): ResponseEntity<Map<String, SollicitationEntity?>> {
        val data = service.findById(id)
        val response = mapOf("sollicitation" to data)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/delete/{id}")
    suspend fun deleteById(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        val sollicitation = service.findById(id) ?: return ResponseEntity.ok(mapOf("message" to "Sollicitation not found"))
        return if(service.deleteById(id)) {
            ResponseEntity.ok(mapOf("message" to "sollicitation deleted successfully"))
        }else{
            return ResponseEntity.ok(mapOf("error" to "Something was wrong"))
        }
    }
}