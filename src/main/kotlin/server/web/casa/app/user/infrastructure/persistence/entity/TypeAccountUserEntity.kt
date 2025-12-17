package server.web.casa.app.user.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.user.domain.model.TypeAccountUser

@Table(name = "type_account_users")
class TypeAccountUserEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("type_account_id")
    val typeAccountId: Long,
    @Column("user_id")
    val userId: Long
)

fun TypeAccountUserEntity.toDomain() = TypeAccountUser(
    id = this.id,
    typeAccountId = this.typeAccountId,
    userId = this.userId,
)