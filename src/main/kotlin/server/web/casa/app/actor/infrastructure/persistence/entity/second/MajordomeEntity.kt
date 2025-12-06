package server.web.casa.app.actor.infrastructure.persistence.entity.second

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "majordomes")
class MajordomeEntity(
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val majordomeId : Long,
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