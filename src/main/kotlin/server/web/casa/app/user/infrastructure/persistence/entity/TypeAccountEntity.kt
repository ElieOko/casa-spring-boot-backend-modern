package server.web.casa.app.user.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "TypeAccounts")
class TypeAccountEntity(
    @Id
    val typeAccountId : Long = 0,
    val name : String,
)