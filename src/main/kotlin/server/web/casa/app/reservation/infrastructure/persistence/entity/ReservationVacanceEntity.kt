package server.web.casa.app.reservation.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.domain.model.ReservationType
import java.time.LocalDate

@Table("reservation_vacances")
data class ReservationVacanceEntity(
   @Id
   @Column("id")
   val id: Long? = null,
   @Column("vacance_id")
   val vacanceId: Long,
   @Column("user_id")
   val userId: Long,
   @Column("message")
   val message: String? = "",
   @Column("reservation_heure")
   val reservationHeure: String? = "",
   @Column("status")
   var status: String = ReservationStatus.PENDING.name,
   @Column("type")
   val type: String = ReservationType.STANDARD.name,
   @Column("is_active")
   var isActive: Boolean = true,
   @Column("cancellation_reason")
   var cancellationReason: String? = "",
   @Column("start_date")
   val startDate: LocalDate,
   @Column("end_date")
   val endDate: LocalDate,
   @Column("created_at")
   val createdAt: LocalDate = LocalDate.now()
)

