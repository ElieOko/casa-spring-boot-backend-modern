package server.web.casa.app.prestation.infrastructure.persistance.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.reservation.domain.model.ReservationStatus
import java.time.LocalDate

@Table("sollicitations")
data class SollicitationEntity(
    @Id
    @Column("id")
    val id: Long? = null,

    @Column("user_id")
    val userId: Long?,

    @Column("prestation_id")
    val prestationId: Long?,

    @Column("devise_id")
    val deviseId: Long?,

    @Column("budget")
    val budget: Double?,

    @Column("description")
    val description: String?,

    @Column("status")
    var status: String = ReservationStatus.PENDING.name,

    @Column("start_date")
    val startDate: LocalDate,

    @Column("end_date")
    val endDate: LocalDate,

    @Column("created_at")
    val createdAt: LocalDate = LocalDate.now()

)