package server.web.casa.app.notification.domain.model.request

import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity

data class NotificationSystemRequest(
    val title : String,
    val description : String,
)

data class NotificationReservation(
    val reservation : ReservationEntity,
    val guestUser : UserEntity,
    val hostUser : UserEntity
)