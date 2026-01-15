package server.web.casa.app.reservation.domain.model

import server.web.casa.app.property.domain.model.Terrain
import server.web.casa.app.property.domain.model.Vacance
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationTerrainEntity
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationVacanceEntity
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDate

data class ReservationVacance(
    val reservationId: Long? = null,
    val userId: Long,
    val vacanceId: Long,
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

data class ReservationVacanceDTO(
    val reservation: ReservationVacanceEntity,
   //val vacance: Vacance,
    val user: UserDto
)