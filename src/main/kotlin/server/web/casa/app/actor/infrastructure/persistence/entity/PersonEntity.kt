package server.web.casa.app.actor.infrastructure.persistence.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity

@Entity
@Table(name = "persons")
class PersonEntity(
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = 0,
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
    @Column("numberCard", nullable = true)
    val numberCard : String? = null,
    @ManyToOne
    @JoinColumn(name = "userId")
    val user : UserEntity?,
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "typeCardId", nullable = true)
    val typeCard : TypeCardEntity? = null,
)