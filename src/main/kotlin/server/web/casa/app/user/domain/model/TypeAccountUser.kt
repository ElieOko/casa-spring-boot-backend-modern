package server.web.casa.app.user.domain.model

import server.web.casa.app.user.infrastructure.persistence.entity.TypeAccountUserEntity

data class TypeAccountUser(
    val id: Long,
    val userId: Long,
    val typeAccountId:Long
)
fun TypeAccountUser.toEntity() = TypeAccountUserEntity(
    typeAccountUserId = this.id,
    typeAccountId = this.typeAccountId,
    userId = this.userId,
)