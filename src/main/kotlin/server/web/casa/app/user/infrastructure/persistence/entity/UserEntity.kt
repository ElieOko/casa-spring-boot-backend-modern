package server.web.casa.app.user.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import server.web.casa.app.actor.infrastructure.persistence.entity.*
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationReservationEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import kotlin.time.*

@Table(name = "users")
@Entity
data class UserEntity @OptIn(ExperimentalTime::class) constructor(
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long = 0,
    @ManyToOne
    @JoinColumn("typeAccountId")
    val typeAccount: TypeAccountEntity,
//    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "city")
    var city: String? = null,
    @Column("password", nullable = false)
    @JsonIgnore
    var password: String? = "",
    @Column("email", nullable = true)
    var email: String? = null,
    @Column("username", nullable = true)
    var username: String? = null,
    @Column("isPremium", nullable = true)
    var isPremium: Boolean = false,
    @Column("isCertified", nullable = true)
    var isCertified: Boolean = false,
    @Column("phone", nullable = true)
    val phone: String?=null,
    @Column("country", nullable = true)
    val country: String? = "Democratic Republic of the Congo",
    @Column("createdAt")
    val createdAt: Instant = Clock.System.now(),
    @OneToMany(mappedBy = "user")
    val properties : List<PropertyEntity> = emptyList(),
    @OneToMany(mappedBy = "guestUser")
    val guestUser : List<NotificationReservationEntity> = emptyList(),
    @OneToMany(mappedBy = "hostUser")
    val hostUser : List<NotificationReservationEntity> = emptyList(),
    @OneToMany(mappedBy = "user")
    val bailleur: List<BailleurEntity> = emptyList(),
    @OneToMany(mappedBy = "user")
    val commissionnaire: List<CommissionnaireEntity> = emptyList(),
    @OneToMany(mappedBy = "user")
    val locataire: List<LocataireEntity> = emptyList(),
    @OneToMany(mappedBy = "parrain")
    val parrainBailleur: List<BailleurEntity> = emptyList(),
    @OneToMany(mappedBy = "user")
    val reservation : List<ReservationEntity> = emptyList()

)
