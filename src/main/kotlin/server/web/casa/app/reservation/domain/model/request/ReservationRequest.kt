package server.web.casa.app.reservation.domain.model.request
import jakarta.validation.constraints.NotNull
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
    val reservationHeure :String
)
