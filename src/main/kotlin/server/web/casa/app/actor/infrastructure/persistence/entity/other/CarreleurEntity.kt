package server.web.casa.app.actor.infrastructure.persistence.entity.other

import jakarta.persistence.*

@Entity
@Table(name = "carreleurs")
class CarreleurEntity(
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val carreleurId : Long,
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
)