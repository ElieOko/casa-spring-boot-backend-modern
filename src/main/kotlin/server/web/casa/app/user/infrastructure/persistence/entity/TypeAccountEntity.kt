package server.web.casa.app.user.infrastructure.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "TypeAccounts")
data class TypeAccountEntity(
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val typeAccountId : Long = 0,
    @Column("name", unique = true)
    val name : String,
    @OneToMany(mappedBy = "typeAccount")
    val user: List<UserEntity> = emptyList()
)