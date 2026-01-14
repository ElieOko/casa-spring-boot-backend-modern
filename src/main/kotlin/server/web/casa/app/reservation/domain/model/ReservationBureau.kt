package server.web.casa.app.reservation.domain.model

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import server.web.casa.app.property.domain.model.Bureau
import server.web.casa.app.property.domain.model.BureauDTO
import server.web.casa.app.property.domain.model.dto.PropertyDTO
import server.web.casa.app.property.domain.model.dto.PropertyMasterDTO
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationBureauEntity
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDate
import javax.annotation.Nullable

data class ReservationBureau(
    val reservationId: Long? = null,
    val userId: Long ?,
    val bureauId: Long?,
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

data class ReservationBureauDTO(
    val reservation: ReservationBureauEntity,
    val bureau: Bureau,
    val user: UserDto,
   // val propietaire:UserDto
)

data class ReservationBureauRequest(
    @NotNull
    val userId: Long,
    @NotNull
    val bureauId: Long,
    @NotNull
    val startDate: LocalDate,
    @Nullable
    val message : String,
    @NotNull
    val endDate: LocalDate,
    @NotNull
    val createdAt: LocalDate = LocalDate.now(),
    @Nullable
    val status : ReservationStatus,
    @Nullable
    val type : ReservationType,

    @NotNull
    @field:Pattern(
        regexp = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$",
        message = "Format invalide, attendu HH:mm:ss"
    )
    val reservationHeure :String
)
