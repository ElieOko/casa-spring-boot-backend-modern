package server.web.casa.app.actor.infrastructure.persistence.entity

import jakarta.persistence.*
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity

@Entity
@Table(name = "locataires")
data class LocataireEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column("id")
    val locataireId : Long = 0,
    @Column("firstName")
    val firstName   : String,
    @Column("lastName")
    val lastName    : String,
    @Column("fullName")
    val fullName    : String,
    @Column("address", nullable = true)
    val address     : String? = null,
    @Column("images", nullable = true)
    val images      : String?,
    @Column("cardFront", nullable = true)
    val cardFront   : String?,
    @Column("cardBack", nullable = true)
    val cardBack    : String?,
    @ManyToOne
    @JoinColumn(name = "userId")
    val user : UserEntity?,
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "typeCardId", nullable = true)
    val typeCard : TypeCardEntity? = null,
    @Column("numberCard", nullable = true)
    val numberCard  : String? = null,
)
