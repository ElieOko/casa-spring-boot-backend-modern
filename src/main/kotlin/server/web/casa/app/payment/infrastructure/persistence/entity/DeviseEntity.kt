package server.web.casa.app.payment.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.payment.domain.model.Devise

@Table(name = "devises")
class DeviseEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("name")
    val name: String,
    @Column("code")
    val code: String,
    @Column("taux_local")
    val tauxLocal: Double? = 22500.0
)

fun DeviseEntity.toDomain() = Devise(
    id = this.id,
    name = this.name,
    code = this.code,
    tauxLocal = this.tauxLocal
)