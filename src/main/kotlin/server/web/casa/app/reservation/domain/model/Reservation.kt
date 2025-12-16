package server.web.casa.app.reservation.domain.model

import java.time.LocalDate

data class Reservation(
    val reservationId: Long = 0,
    val user: Long ?,
    val property: Long,
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
