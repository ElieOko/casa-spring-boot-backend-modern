package server.web.casa.app.prestation.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.prestation.application.CotationPrestationService
import server.web.casa.app.prestation.application.SollicitationService
import server.web.casa.app.prestation.domain.request.CotationRequest
import server.web.casa.app.prestation.infrastructure.persistance.entity.CotationPrestationEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.sollicitation.CotationRoute
import server.web.casa.utils.Mode
import java.time.LocalDateTime

const val ROUTE_COTATION = CotationRoute.COTATION

@Tag(name = "Cotation", description = "cotation de prestation")
@RestController
@RequestMapping(ROUTE_COTATION)
@Profile(Mode.DEV)
class CotationPrestationController(
    private val service: CotationPrestationService,
    private  val userS: UserService,
    private  val solS: SollicitationService,
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createCotation(
        @Valid @RequestBody req: CotationRequest
    ):ResponseEntity <Map<String, Any?>>{
        val checkUser = userS.findIdUser(req.userId) ?: return ResponseEntity.badRequest().body(mapOf("error" to "user not found"))
        val checkSollicitation = solS.findById(req.sollicitationId) ?: return ResponseEntity.badRequest().body(mapOf("error" to "prestation not found"))
        val prestateur = userS.findIdUser(checkSollicitation.user.userId!!)
       val checkCoteMaxFive = if (req.cote >= 6 ) return ResponseEntity.badRequest()
                                .body(mapOf("error" to "Cote doit etre inferieure ou egale à 5"))
                                else null

        val data = CotationPrestationEntity(
            userId = checkUser.userId!! ,
            sollicitationId = checkSollicitation.sollicitation.id!!,
            cote = req.cote,
            commentaire = req.commentaire,
            isActive = true,
            createdAt = LocalDateTime.now()
        )
        val created = service.create(data)
        return ResponseEntity.ok(mapOf(
            "cotation" to created,
            "prestateur" to prestateur,
            "evaluateur" to checkUser))
    }

    @GetMapping("/",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllCotations(): ResponseEntity<Map<String, List<CotationPrestationEntity>>>{
        return ResponseEntity.ok().body(
            mapOf(
                "cotation" to service.findAll().toList()
            ))
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getCotationById(@PathVariable id: Long): ResponseEntity<Map<String, CotationPrestationEntity?>> {
        val data = service.findById(id)
        val response = mapOf("cotation" to data)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/delete/{id}")
    suspend fun deleteById(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        val cotation = service.findById(id) ?: return ResponseEntity.ok(mapOf("message" to "cotation not found"))
        return if(service.deleteById(id)) {
            ResponseEntity.ok(mapOf("message" to "cotation deleted successfully"))
        }else{
            return ResponseEntity.ok(mapOf("error" to "Something was wrong"))
        }
    }
}