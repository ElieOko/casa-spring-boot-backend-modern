package server.web.casa.app.user.infrastructure.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "accounts")
data class AccountEntity(
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,
    @Column("name", unique = true)
    val name : String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn("typeAccountId")
    val typeAccount: TypeAccountEntity,
)