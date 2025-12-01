package server.web.casa.app.actor.infrastructure.persistence.entity

import jakarta.persistence.*
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity

@Entity
@Table(name = "commissionnaires")
data class CommissionnaireEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column("id")
    val commissionnaireId : Long,
    @Column("firstName")
    val firstName   : String,
    @Column("lastName")
    val lastName    : String,
    @Column("fullName")
    val fullName    : String,
    @Column("address", nullable = true)
    val address     : String? = null,
    @Column("images", nullable = true)
    val images      : String? = null,
    @Column("cardFront", nullable = true)
    val cardFront   : String? = null,
    @Column("cardBack", nullable = true)
    val cardBack    : String? = null,
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "userId")
    val user : UserEntity?,
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "typeCardId")
    val typeCard : TypeCardEntity?,
    @Column("numberCard", nullable = true)
    val numberCard  : String? = null
)
