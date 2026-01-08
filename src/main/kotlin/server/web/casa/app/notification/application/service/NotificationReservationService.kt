package server.web.casa.app.notification.application.service

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.notification.domain.model.request.NotificationReservation
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationReservationEntity
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationReservationRepository
import server.web.casa.app.reservation.application.service.ReservationService

@Service
class  NotificationReservationService(
    private val repository: NotificationReservationRepository,
    private val reservationService: ReservationService
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

    suspend fun dealConcludedHost(reservation : Long, state : Boolean): Boolean {
      val data = repository.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserDealConcluded = state
        repository.save(data)
        return state
    }

    suspend fun dealConcludedGuest(reservation : Long, state : Boolean): Boolean {
        val data = repository.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserDealConcluded = state
        repository.save(data)
        return state
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
        return false
    }
    suspend fun deleteByReservation(reservation: Long): Boolean {
        val notificationDelete = repository.findAll().filter { entity -> entity.reservationId == reservation }.toList()
        repository.deleteAll(notificationDelete)
        return notificationDelete.isNotEmpty()
    }
}