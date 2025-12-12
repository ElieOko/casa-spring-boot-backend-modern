package server.web.casa.app.user.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("accounts")
class AccountEntity(
    @Id
    val id : Long = 0,
    val name : String,
    val typeAccountId: Long,
)