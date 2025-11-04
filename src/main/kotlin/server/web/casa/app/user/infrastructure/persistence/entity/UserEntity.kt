package server.web.casa.app.user.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import server.web.casa.app.actor.infrastructure.persistence.entity.BailleurEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.CommissionnaireEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.LocataireEntity
import server.web.casa.app.address.infrastructure.persistence.entity.CityEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Table(name = "users")
@Entity
data class UserEntity @OptIn(ExperimentalTime::class) constructor(
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn("typeAccountId")
    val typeAccount: TypeAccountEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityId")
    var city: CityEntity,
    @Column("password", nullable = false)
    @JsonIgnore
    var password: String?,
    @Column("email", nullable = true)
    var email: String? = null,
    @Column("phone", nullable = true)
    val phone: String,
    @Column("createdAt")
    val createdAt: Instant = Clock.System.now(),
    @OneToMany(mappedBy = "user")
    val properties : List<PropertyEntity> = emptyList(),
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
