package server.web.casa.app.user.domain.model

import server.web.casa.app.user.infrastructure.persistence.entity.TypeAccountUserEntity

data class TypeAccountUser(
    val id: Long? = null,
    val userId: Long,
    val typeAccountId:Long
)
fun TypeAccountUser.toEntity() = TypeAccountUserEntity(
    id = this.id,
    typeAccountId = this.typeAccountId,
    userId = this.userId,
)