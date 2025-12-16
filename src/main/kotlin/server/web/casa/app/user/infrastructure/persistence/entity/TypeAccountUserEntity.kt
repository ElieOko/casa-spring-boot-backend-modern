package server.web.casa.app.user.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.user.domain.model.TypeAccountUser

@Table(name = "TypeAccountUsers")
class TypeAccountUserEntity(
    @Id
    val typeAccountUserId : Long = 0,
    val typeAccountId: Long,
    val userId: Long,
)

fun TypeAccountUserEntity.toDomain() = TypeAccountUser(
    id = this.typeAccountUserId,
    typeAccountId = this.typeAccountId,
    userId = this.userId,
)