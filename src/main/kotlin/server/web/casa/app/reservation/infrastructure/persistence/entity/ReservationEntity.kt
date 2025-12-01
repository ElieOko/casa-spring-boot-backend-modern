package server.web.casa.app.reservation.infrastructure.persistence.entity

import jakarta.persistence.*
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationReservationEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.reservation.domain.model.*
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import java.time.LocalDate

@Table(name = "reservations")
@Entity
 class ReservationEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val reservationId       : Long = 0,
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn("propertyId")
    val property            : PropertyEntity,
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn("userId")
    val user                : UserEntity ?,
    @OneToMany(
        mappedBy = "reservation",
        cascade = [CascadeType.REMOVE],
        orphanRemoval = true
    )
    val notification: MutableList<NotificationReservationEntity> = mutableListOf(),
    @Column(name = "message", nullable = true)
    val message             : String? = "",
    @Column(name = "reservationHeure", nullable = true)
    val reservationHeure    : String? = "",
    @Column(name = "status")
    val status              : ReservationStatus = ReservationStatus.PENDING,
    @Column("type_reservation")
    val type                : ReservationType = ReservationType.STANDARD,
    @Column(name = "isActive")
    val isActive            : Boolean = true,
    @Column(name = "cancellationReason", nullable = true)
    val cancellationReason  : String? = "",
    @Column(name = "startDate")
    val startDate           : LocalDate,
    @Column(name = "endDate")
    val endDate             : LocalDate,
    @Column("createdAt")
    val createdAt           : LocalDate = LocalDate.now(),
)
