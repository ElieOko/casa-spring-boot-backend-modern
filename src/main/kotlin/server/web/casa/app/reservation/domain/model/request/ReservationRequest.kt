package server.web.casa.app.reservation.domain.model.request
import jakarta.validation.constraints.NotNull
import server.web.casa.app.property.domain.model.request.PropertyRequest
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.user.domain.model.UserRequest
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import java.time.LocalDate

data class ReservationRequest(
    @NotNull
    val reservationId: Long,
    @NotNull
    val user: UserEntity,
    @NotNull
    val property: PropertyEntity,
    @NotNull
    val startDate: LocalDate,
    @NotNull
    val message : String,
    @NotNull
    val endDate: LocalDate,
    @NotNull
    val createdAt: LocalDate = LocalDate.now(),
)
