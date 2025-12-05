package server.web.casa.app.pub.infrastructure.persistance.entity

import jakarta.persistence.*
import server.web.casa.app.pub.domain.model.Image
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import java.time.LocalDate

@Table(name = "publicity")
@Entity
class PublicityEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val publicityId: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user            : UserEntity?,

    @Embedded
    val image           : Image? = null,

    @Column(nullable = false, length = 100)
    val title           : String,

    @Column(nullable = false, length = 500)
    val description     : String,

    @Column("isActive")
    var isActive        : Boolean = true,

    @Column("createdAt")
    val createdAt       : LocalDate = LocalDate.now(),
)
