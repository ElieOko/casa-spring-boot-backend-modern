package server.web.casa.utils.scheduler

import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*
import org.springframework.stereotype.Service
import server.web.casa.app.notification.application.service.NotificationReservationService
import server.web.casa.app.notification.application.service.NotificationService
import server.web.casa.app.notification.domain.model.request.TagType
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationCasaEntity
import server.web.casa.app.notification.infrastructure.persistence.entity.toDomain
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationCasaRepository
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyRepository
import server.web.casa.app.reservation.application.service.ReservationService
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.repository.*

@Service
class ReservationTaskService(
   // private val reservationRepository: ReservationRepository,
   private val scheduler: ReservationScheduler,
   private val reservationProperty : ReservationRepository,
   private val reservationBureau: ReservationBureauRepository,
   private val reservationFestive: ReservationFestiveRepository,
   private val reservationFuneraire: ReservationFuneraireRepository,
   private val reservationHotel: ReservationHotelRepository,
   private val reservationTerrain: ReservationTerrainRepository,
   private val reservationVacance: ReservationVacanceRepository,
   private val notif: NotificationReservationService,
   private val notificationService: NotificationService,
   private val notification2 : NotificationCasaRepository,
   private val propertyR: PropertyRepository,
   private val service: ReservationService,

) {
    private val log = LoggerFactory.getLogger(this::class.java)
    suspend fun createReservation(taskId: Long, hoursUntilExecution: Long, taskType: String) {
        val expireAt = Instant.now().plusSeconds(hoursUntilExecution * 3600)
        //scheduler.scheduleOneShot(taskId, taskType, expireAt)
    }

    suspend fun executeTask(reservationId: Long, taskType: String, type: String = "") {
        // logique X ou Y
        when(type){
            "property" -> {
                val data = reservationProperty.findById(reservationId)
                if (data != null) {
                    data.status = ReservationStatus.CANCELLED.name
                    reservationProperty.save(data)
                    val reservation = service.findById(reservationId)?.reservation
                    val propertyEntity = propertyR.findById(reservation?.propertyId!!)
                    val note = notification2.save(NotificationCasaEntity(id = null, userId = propertyEntity?.user, title = "Annulation de visite", message = "Le client a annulé sa demande de visite. Aucune action n’est requise.", tag = TagType.DEMANDES.toString(),))
                    notificationService.sendNotificationToUser(propertyEntity?.user.toString(),note.toDomain())
//                    val response = mapOf("DealCancel" to notification, "message" to "Deal cancel successfully")
                }
            }
            "bureau" -> {
                val data = reservationBureau.findById(reservationId)
                if (data != null) {
                    data.status = ReservationStatus.CANCELLED.name
                    reservationBureau.save(data)
                }
            }
            "festive" ->{
                val data = reservationFestive.findById(reservationId)
                if (data != null) {
                    data.status = ReservationStatus.CANCELLED.name
                    reservationFestive.save(data)
                }
            }
            "funeraire" ->{
                val data = reservationFuneraire.findById(reservationId)
                if (data != null) {
                    data.status = ReservationStatus.CANCELLED.name
                    reservationFuneraire.save(data)
                }
            }
            "hotel" ->{
                val data = reservationHotel.findById(reservationId)
                if (data != null) {
                    data.status = ReservationStatus.CANCELLED.name
                    reservationHotel.save(data)
                }
            }
            "terrain" ->{
                val data = reservationTerrain.findById(reservationId)
                if (data != null) {
                    data.status = ReservationStatus.CANCELLED.name
                    reservationTerrain.save(data)
                }
            }
            "vacance" ->{
                val data = reservationVacance.findById(reservationId)
                if (data != null) {
                    data.status = ReservationStatus.CANCELLED.name
                    reservationVacance.save(data)
                }
            }
            else ->  log.info("Pas d'execution type fournit invalide !!!")
        }
        println("Exécution tâche $taskType pour la réservation $reservationId")
        log.info("Exécution tâche $taskType pour la réservation $reservationId")
    }

}