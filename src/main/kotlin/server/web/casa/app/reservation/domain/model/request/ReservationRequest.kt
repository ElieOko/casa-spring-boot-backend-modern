package server.web.casa.app.reservation.domain.model.request
import jakarta.validation.constraints.*
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.domain.model.ReservationType
import java.time.LocalDate
import javax.annotation.*

data class ReservationRequest(
    @NotNull
    val userId: Long,
    @NotNull
    val propertyId: Long,
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
