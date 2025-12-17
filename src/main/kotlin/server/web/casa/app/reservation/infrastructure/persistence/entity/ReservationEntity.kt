package server.web.casa.app.reservation.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.domain.model.ReservationType
import java.time.LocalDate

@Table("reservations")
data class ReservationEntity(
   @Id
   @Column("id")
   val id: Long? = null,
   @Column("property_id")
   val propertyId: Long,
   @Column("user_id")
   val userId: Long? = null,
   @Column("message")
   val message: String? = "",
   @Column("reservation_heure")
   val reservationHeure: String? = "",
   @Column("status")
   val status: String = ReservationStatus.PENDING.name,
   @Column("type")
   val type: String = ReservationType.STANDARD.name,
   @Column("is_active")
   val isActive: Boolean = true,
   @Column("cancellation_reason")
   val cancellationReason: String? = "",
   @Column("start_date")
   val startDate: LocalDate,
   @Column("end_date")
   val endDate: LocalDate,
   @Column("created_at")
   val createdAt: LocalDate = LocalDate.now()
)

