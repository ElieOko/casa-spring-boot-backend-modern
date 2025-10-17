package server.web.casa.app.actor.infrastructure.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "typecards")
data class TypeCardEntity(
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val typeCardId : Long? = 0,
    @Column("name")
    val name : String?,
    @OneToMany(mappedBy = "typeCard")
    val bailleur: List<BailleurEntity> = emptyList(),
    @OneToMany(mappedBy = "typeCard")
    val locataire: List<LocataireEntity> = emptyList(),
    @OneToMany(mappedBy = "typeCard")
    val commissionnaire: List<CommissionnaireEntity> = emptyList(),
)
