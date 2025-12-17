package server.web.casa.app.user.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.user.domain.model.Account
import server.web.casa.app.user.domain.model.TypeAccount

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
data class AccountDTO(
    val id : Long? = null,
    val name: String,
    val typeAccount : TypeAccount,
)
fun AccountEntity.toTypeAccount(name: String) = TypeAccount(
    typeAccountId = this.typeAccountId,
    name = name,
)
fun AccountEntity.toDomain() = Account(id = this.id,name = this.name, typeAccountId = this.typeAccountId)