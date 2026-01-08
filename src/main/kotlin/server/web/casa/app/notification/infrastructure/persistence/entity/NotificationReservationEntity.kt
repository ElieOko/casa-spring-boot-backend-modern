package server.web.casa.app.notification.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import java.time.LocalDate

@Table("notification_reservations")
data class NotificationReservationEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("reservation_id")
    val reservationId: Long,
    @Column("guest_user_id")
    val guestUserId: Long,
    @Column("host_user_id")
    val hostUserId: Long,
    @Column("host_user_state")
    var hostUserState: Boolean? = null,
    @Column("guest_user_state")
    var guestUserState: Boolean? = null,
    @Column("guest_user_deal_concluded")
    var guestUserDealConcluded: Boolean? = null,
    @Column("host_user_deal_concluded")
    var hostUserDealConcluded: Boolean? = null,
    @Column("date_updated")
    val dateUpdated: LocalDate = LocalDate.now(),
    @Column("date_created")
    val dateCreated: LocalDate = LocalDate.now()
)