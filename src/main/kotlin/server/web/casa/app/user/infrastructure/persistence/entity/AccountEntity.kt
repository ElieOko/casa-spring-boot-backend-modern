package server.web.casa.app.user.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.user.domain.model.Account

@Table("accounts")
class AccountEntity(
    @Id
    val id : Long = 0,
    val name : String,
    val typeAccountId: Long,
)
fun AccountEntity.toDomain() = Account(id = this.id,name = this.name, typeAccountId = this.typeAccountId)