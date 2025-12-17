package server.web.casa.app.user.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.user.domain.model.Account

@Table("accounts")
class AccountEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("name")
    val name: String,
    @Column("type_account_id")
    val typeAccountId: Long
)
fun AccountEntity.toDomain() = Account(id = this.id,name = this.name, typeAccountId = this.typeAccountId)