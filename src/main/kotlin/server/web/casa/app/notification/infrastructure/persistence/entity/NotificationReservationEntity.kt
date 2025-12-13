package server.web.casa.app.notification.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import java.time.LocalDate

@Table(name = "notification_reservations")
class NotificationReservationEntity(
    @Id
    val id : Long = 0,
    val reservationId : Long,
    val guestUserId : Long,
    val hostUserId : UserEntity,
    var hostUserState : Boolean? = null,
    var guestUserState: Boolean? = null,
    var guestUserDealConcluded: Boolean? = null,
    var hostUserDealConcluded: Boolean? = null,
    val dateUpdated : LocalDate = LocalDate.now(),
    val dateCreated : LocalDate = LocalDate.now()
)