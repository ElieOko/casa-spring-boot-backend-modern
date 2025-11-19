package server.web.casa.app.notification.infrastructure.persistence.entity

import jakarta.persistence.*
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import java.time.LocalDate

@Entity
@Table(name = "notification_reservations")
class NotificationReservationEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column("id")
    val notificationReservationId : Long = 0,
    @ManyToOne
    @JoinColumn("reservation_id")
    val reservation : ReservationEntity,
    @ManyToOne
    @JoinColumn("guest_user_id")
    val guestUser : UserEntity,
    @ManyToOne
    @JoinColumn("host_user_id")
    val hostUser : UserEntity,
    @Column("host_user_state")
    var hostUserState : Boolean? = null,
    @Column("guest_user_state")
    var guestUserState: Boolean? = null,
    @Column("guest_user_deal_concluded")
    var guestUserDealConcluded: Boolean? = null,
    @Column("host_user_deal_concluded")
    var hostUserDealConcluded: Boolean? = null,
    @Column("dateUpdated")
    val dateUpdated : LocalDate = LocalDate.now(),
    @Column("dateCreated")
    val dateCreated : LocalDate = LocalDate.now()
)