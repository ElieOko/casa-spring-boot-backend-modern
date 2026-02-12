package server.web.casa.app.reservation.domain.model

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import server.web.casa.app.property.domain.model.Vacance
import server.web.casa.app.property.infrastructure.persistence.entity.VacanceImageEntity
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationVacanceEntity
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDate
import javax.annotation.Nullable

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
    val vacance: Vacance?,
    val user: UserDto,
    val imageVacance: List<VacanceImageEntity>
)

data class ReservationVacanceRequest(
    @NotNull
    val userId: Long,
    @NotNull
    val vacanceId: Long,
    @NotNull
    val startDate: LocalDate,
    @Nullable
    val message : String,
    @NotNull
    val endDate: LocalDate,
    @Nullable
    val type : ReservationType,
    @NotNull
    @field:Pattern(
        regexp = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$",
        message = "Format invalide, attendu HH:mm:ss"
    )
    val reservationHeure :String
)