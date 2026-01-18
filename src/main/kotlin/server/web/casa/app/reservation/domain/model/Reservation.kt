package server.web.casa.app.reservation.domain.model

import server.web.casa.app.property.domain.model.dto.PropertyDTO
import server.web.casa.app.property.domain.model.dto.PropertyMasterDTO
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDate

data class Reservation(
    val reservationId: Long? = null,
    val user: Long ?,
    val property: Long?,
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

data class ReservationDTO(
    val reservation: ReservationEntity?,
    val property: PropertyMasterDTO?,
    val user: UserDto
)