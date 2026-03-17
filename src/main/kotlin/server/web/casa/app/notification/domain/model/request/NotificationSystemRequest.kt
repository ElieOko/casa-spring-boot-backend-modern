package server.web.casa.app.notification.domain.model.request

import server.web.casa.app.reservation.infrastructure.persistence.entity.*
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

data class NotificationReservationFestive(
    val reservation : ReservationFestiveEntity,
    val guestUser : UserEntity,
    val hostUser : UserEntity
)

data class NotificationReservationFuneraire(
    val reservation : ReservationFuneraireEntity,
    val guestUser : UserEntity,
    val hostUser : UserEntity
)

data class NotificationReservationHotel(
    val reservation : ReservationChambreHotelEntity,
    val guestUser : UserEntity,
    val hostUser : UserEntity
)

data class NotificationReservationTerrain(
    val reservation : ReservationTerrainEntity,
    val guestUser : UserEntity,
    val hostUser : UserEntity
)

data class NotificationReservationVacance(
    val reservation : ReservationVacanceEntity,
    val guestUser : UserEntity,
    val hostUser : UserEntity
)

data class NotificationReservationBureau(
    val reservation : ReservationBureauEntity,
    val guestUser : UserEntity,
    val hostUser : UserEntity
)