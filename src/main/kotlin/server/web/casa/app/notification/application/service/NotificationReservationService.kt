package server.web.casa.app.notification.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.notification.domain.model.request.NotificationReservation
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationReservationEntity
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationReservationRepository
import server.web.casa.app.reservation.application.service.ReservationService

@Service
class NotificationReservationService(
    private val repository: NotificationReservationRepository,
    private val reservationService: ReservationService
) {
    fun create(notice: NotificationReservation): Boolean {
       repository.save(NotificationReservationEntity(
           reservation = notice.reservation,
           guestUser = notice.guestUser,
           hostUser = notice.hostUser,
           guestUserState = true
       )).let {
           return true
       }
    }

    fun dealConcludedHost(reservation : Long, state : Boolean): Boolean {
      val data = repository.findAll().filter { entity ->
            entity.reservation.reservationId == reservation
        }[0]
        data.hostUserDealConcluded = state
        repository.save(data)
        return state
    }

    fun dealConcludedGuest(reservation : Long, state : Boolean): Boolean {
        val data = repository.findAll().filter { entity ->
            entity.reservation.reservationId == reservation
        }[0]
        data.guestUserDealConcluded = state
        repository.save(data)
        return state
    }

    fun stateReservationHost(reservation : Long, state : Boolean): Boolean {
        val data = repository.findAll().filter { entity ->
            entity.reservation.reservationId == reservation
        }[0]
        data.hostUserState = state
        repository.save(data)
        return state
    }

    fun stateReservationGuestCancel(reservation : Long): Boolean {
        val data = repository.findAll().filter { entity ->
            entity.reservation.reservationId == reservation
        }[0]
        data.guestUserState = false
        repository.save(data)
        return false
    }


}