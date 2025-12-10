package server.web.casa.app.user.infrastructure.persistence.entity

import jakarta.persistence.*
import server.web.casa.app.user.domain.model.User

@Entity
@Table(name = "TypeAccountUsers")
data class TypeAccountUserEntity(
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val typeAccountId : Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn("typeAccountId")
    val typeAccount: TypeAccountEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn("userId")
    val user: UserEntity,

)