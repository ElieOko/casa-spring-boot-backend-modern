package server.web.casa.app.property.domain.model

import server.web.casa.app.property.infrastructure.persistence.entity.SectorEntity

data class Sector(
    val id: Long? = null,
    val name: String,
    val isActive : Boolean = true,
)

fun Sector.toEntity()= SectorEntity(
    id = this.id,
    name = this.name,
    isActive = this.isActive
)