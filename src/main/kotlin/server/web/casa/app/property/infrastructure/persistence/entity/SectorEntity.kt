package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.property.domain.model.Sector

@Table(name = "sectors")
class SectorEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("name")
    val name: String,
    @Column("description")
    val isActive : Boolean = true,
)

fun SectorEntity.toDomain() = Sector(
    id = this.id,
    name = this.name,
    isActive = this.isActive
)