package server.web.casa.app.prestation.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.ecosystem.application.service.PrestationService
import server.web.casa.app.notification.application.service.NotificationService
import server.web.casa.app.notification.domain.model.request.TagType
import server.web.casa.app.notification.infrastructure.persistence.entity.*
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationCasaRepository
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.prestation.application.SollicitationService
import server.web.casa.app.prestation.domain.model.SollicitationDTO
import server.web.casa.app.prestation.domain.request.SollicitationRequest
import server.web.casa.app.prestation.infrastructure.persistance.entity.SollicitationEntity
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.sollicitation.SollicitationScope
import server.web.casa.utils.Mode


@Tag(name = "Sollicitation", description = "Sollicitation de prestation")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)
class SollicitatonController(
    private val service: SollicitationService,
    private  val userS: UserService,
    private  val prestS: PrestationService,
    private  val deviseS: DeviseService,
    private val notificationService: NotificationService,
    private val notification2 : NotificationCasaRepository,
) {
    @PostMapping("/{version}/${SollicitationScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
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
            budget = req.budget,
            description = req.description,
            startDate = req.startDate,
            endDate = req.endDate
        )
        val created = service.create(data)
        val note = notification2.save(NotificationCasaEntity(id = null, userId = checkPrestation.userId, title = "Demande d'intervention reçue", message = "Un client est intéressé par votre service et souhaite que vous intervennez. Ne tardez pas à répondre \uD83D\uDE09", tag = TagType.DEMANDES.toString(),))
        notificationService.sendNotificationToUser(checkPrestation.userId.toString(),note.toDomain())
        return ResponseEntity.ok(mapOf(
            "sollicitation" to created,
            "prestateur" to prestateur,
            "demandeur" to checkUser))
    }

    @GetMapping("/{version}/${SollicitationScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllSollicitations(): ResponseEntity<Map<String, List<SollicitationDTO>>>{
        return ResponseEntity.ok().body(mapOf("sollicitation" to service.findAll().toList()))
    }

    @GetMapping("/{versiom}/${SollicitationScope.PRIVATE}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getSollicitationById(@PathVariable id: Long): ResponseEntity<Map<String, SollicitationDTO?>> {
        val data = service.findById(id)
        val response = mapOf("sollicitation" to data)
        return ResponseEntity.ok(response)
    }
    @GetMapping("/{version}/${SollicitationScope.PROTECTED}/user/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getByUserId(@PathVariable userId: Long): ResponseEntity<Map<String, List<SollicitationDTO>?>>{
        val find = service.findByUserId(userId)
        return ResponseEntity.ok(mapOf("data" to find))
    }
    @GetMapping("/{version}/${SollicitationScope.PROTECTED}/user/host/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun findByHostUser(@PathVariable userId:Long): ResponseEntity<Map<String,  List<SollicitationDTO>? >> {
        val user = userS.findIdUser(userId)
        val reservation = service.findByHostUser(user.userId!!)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)

    }
    @PutMapping("/{version}/${SollicitationScope.PROTECTED}/update/status/{id}")
    suspend fun updateSollicitationStatus(
        @PathVariable id: Long,
        @RequestBody request:RequestUpdateStatus
    ) = coroutineScope {

        val userRequest = userS.findIdUser(request.userId)
        val sollicitation: SollicitationDTO? = (service.findById(id) ?: ResponseEntity.ok(mapOf("error" to "sollicitation not found"))) as SollicitationDTO?

        val userId = sollicitation?.user!!.userId
        val prestateurId = prestS.getById ( sollicitation.prestation.id!!)?.userId

        val prestateurCheck = userRequest.userId == prestateurId
        val sollicitateur = userRequest.userId == userId

        if(prestateurCheck || sollicitateur){
            if (prestateurCheck){
                val updated = service.updateStatus(sollicitation.sollicitation,  request.status)
                ResponseEntity.ok(mapOf("sollicitation" to updated))
            }

            if(request.status != ReservationStatus.APPROVED){
                val updated = service.updateStatus(sollicitation.sollicitation,  request.status)
                ResponseEntity.ok(mapOf("sollicitation" to updated))
            }
        }
        ResponseEntity.ok(mapOf("error" to "Authorization denied"))
    }

    @DeleteMapping("/{versiom}/${SollicitationScope.PROTECTED}/delete/{id}")
    suspend fun deleteById(@PathVariable id: Long): ResponseEntity<Map<String, String>> = coroutineScope {
        val sollicitation = service.findById(id) ?: ResponseEntity.ok(mapOf("message" to "Sollicitation not found"))
        if(service.deleteById(id)) {
            ResponseEntity.ok(mapOf("message" to "sollicitation deleted successfully"))
        }else{
            ResponseEntity.ok(mapOf("error" to "Something was wrong"))
        }
    }
}
data class RequestUpdateStatus(
    val userId: Long,
    val status: ReservationStatus
)