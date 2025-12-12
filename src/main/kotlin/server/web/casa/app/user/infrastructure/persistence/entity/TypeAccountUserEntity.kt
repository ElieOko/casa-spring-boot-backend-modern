package server.web.casa.app.user.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "TypeAccountUsers")
class TypeAccountUserEntity(
    @Id
    val typeAccountUserId : Long = 0,
    val typeAccountId: Long,
    val userId: Long,
)