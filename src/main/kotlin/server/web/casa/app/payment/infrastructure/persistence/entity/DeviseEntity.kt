package server.web.casa.app.payment.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.payment.domain.model.Devise

@Table(name = "devises")
class DeviseEntity(
    @Id
    val id : Long = 0,
    val name : String,
    val code : String,
    val tauxLocal : Double? = 22500.0,
)

fun DeviseEntity.toDomain() = Devise(
    id = this.id,
    name = this.name,
    code = this.code,
    tauxLocal = this.tauxLocal
)