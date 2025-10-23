package server.web.casa.app.reservation.domain.model

import server.web.casa.app.property.domain.model.Property
import server.web.casa.app.user.domain.model.User
import java.time.LocalDate

data class Reservation(
    val reservationId: Long = 0,
    val user: User ?,
    val property: Property,
    val message: String? = "",
    val status: ReservationStatus = ReservationStatus.PENDING,
    val type: ReservationType = ReservationType.STANDARD,
    val isActive: Boolean = true,
    val reservationHeure: String? = "",
    val cancellationReason: String? = "",
    val startDate: LocalDate,
    val endDate: LocalDate,
    val createdAt: LocalDate = LocalDate.now(),
)
