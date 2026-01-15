package server.web.casa.app.reservation.domain.model

import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationHotelEntity
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDate

data class ReservationHotel(
    val reservationId: Long? = null,
    val userId: Long,
    val hotelId: Long,
    val message: String? = "",
    val status: String = ReservationStatus.PENDING.name,
    val type: String = ReservationType.STANDARD.name,
    val isActive: Boolean = true,
    val reservationHeure: String? = "",
    val cancellationReason: String? = "",
    val startDate: LocalDate,
    val endDate: LocalDate,
    val createdAt: LocalDate = LocalDate.now(),
)

data class ReservationHotelDTO(
    val reservation: ReservationHotelEntity,
   // val hotel: Hotel,
    val user: UserDto
)