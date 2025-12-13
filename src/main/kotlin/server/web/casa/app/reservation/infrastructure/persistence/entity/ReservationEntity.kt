package server.web.casa.app.reservation.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.reservation.domain.model.*
import java.time.LocalDate

@Table(name = "reservations")
 class ReservationEntity(
    @Id
    val id       : Long = 0,
    val propertyId            : Long,
    val userId                : Long ?,
    val message             : String? = "",
    val reservationHeure    : String? = "",
    val status              : String = ReservationStatus.PENDING.name,
    val type                : String = ReservationType.STANDARD.name,
    val isActive            : Boolean = true,
    val cancellationReason  : String? = "",
    val startDate           : LocalDate,
    val endDate             : LocalDate,
    val createdAt           : LocalDate = LocalDate.now(),
)
