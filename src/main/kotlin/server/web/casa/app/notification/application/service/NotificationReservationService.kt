package server.web.casa.app.notification.application.service

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.notification.domain.model.request.NotificationReservation
import server.web.casa.app.notification.domain.model.request.TagType
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationCasaEntity
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationReservationEntity
import server.web.casa.app.notification.infrastructure.persistence.entity.toDomain
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationCasaRepository
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationReservationRepository
import server.web.casa.app.reservation.application.service.ReservationService
import server.web.casa.app.user.application.service.UserService
import kotlin.collections.mapOf

@Service
class  NotificationReservationService(
    private val repository: NotificationReservationRepository,
    private val reservationService: ReservationService,
    private val notificationService: NotificationService,
    private val notification : NotificationCasaRepository,
) {
    suspend fun create(notice: NotificationReservation): Boolean {

       repository.save(NotificationReservationEntity(
           reservationId = notice.reservation.id!!,
           guestUserId = notice.guestUser.userId!!,
           hostUserId = notice.hostUser.userId!!,
           guestUserState = true
       )).let {
           return true
       }
    }

    suspend fun dealConcludedHost(reservation : Long, state : Boolean): Map<String, Any> {
      val data = repository.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserDealConcluded = state
        repository.save(data)
//        val note = notification.save(
//            NotificationCasaEntity(
//                id = null,
//                userId = data.hostUserId,
//                title = "Rendez-vous validé",
//                message = "La visite est confirmée. Tout est prêt pour accueillir le client.",
//                tag = TagType.DEMANDES.toString(),
//            )
//        )
//        notificationService.sendNotificationToUser(data.hostUserId.toString(),note.toDomain())
        return mapOf("state" to state, "host" to data.hostUserId)
    }

    suspend fun dealConcludedGuest(reservation : Long, state : Boolean): Map<String, Any> {
        val data = repository.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserDealConcluded = state
        repository.save(data)
        return mapOf("state" to state, "guest" to data.guestUserId)
    }

    suspend fun stateReservationHost(reservation : Long, state : Boolean): Boolean {
        val data = repository.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserState = state
        repository.save(data)
        return state
    }

    suspend fun stateReservationGuestCancel(reservation : Long): Boolean {
        val data = repository.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserState = false
        repository.save(data)
//        val note = notification.save(
//            NotificationCasaEntity(
//                id = null,
//                userId = data.hostUserId,
//                title = "Annulation de visite",
//                message = "Le client a annulé sa demande de visite. Aucune action n’est requise.",
//                tag = TagType.DEMANDES.toString(),
//            )
//        )
//        notificationService.sendNotificationToUser(data.hostUserId.toString(),note.toDomain())
        return false
    }
    suspend fun deleteByReservation(reservation: Long): Boolean {
        val notificationDelete = repository.findAll().filter { entity -> entity.reservationId == reservation }.toList()
        repository.deleteAll(notificationDelete)
        return notificationDelete.isNotEmpty()
    }
}