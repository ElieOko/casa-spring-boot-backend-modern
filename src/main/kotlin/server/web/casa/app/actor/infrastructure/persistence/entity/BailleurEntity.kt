package server.web.casa.app.actor.infrastructure.persistence.entity

import jakarta.persistence.*
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity

@Entity
@Table(name = "bailleurs")
data class BailleurEntity(
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val bailleurId : Long,
    @Column("firstName")
    val firstName : String,
    @Column("lastName")
    val lastName : String,
    @Column("fullName")
    val fullName : String,
    @Column("address", nullable = true)
    val address : String? = "",
    @Column("images", nullable = true)
    val images : String? = null,
    @Column("cardFront", nullable = true)
    val cardFront : String?,
    @Column("cardBack", nullable = true)
    val cardBack : String? = null,
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "parrainId", nullable = true)
    val parrain : UserEntity? = null,
    @ManyToOne
    @JoinColumn(name = "userId")
    val user : UserEntity?,
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "typeCardId", nullable = true)
    val typeCard : TypeCardEntity? = null,
    @Column("numberCard", nullable = true)
    val numberCard : String? = null,
    @Column("note", nullable = true)
    val note : String? = null
)
