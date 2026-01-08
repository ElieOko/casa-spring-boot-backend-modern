package server.web.casa.app.address.infrastructure.persistence.mapper

import server.web.casa.app.address.domain.model.Quartier
import server.web.casa.app.address.infrastructure.persistence.entity.QuartierEntity

fun QuartierEntity.toDomain() = Quartier(quartierId = this.id, name = this.name)

fun Quartier.toEntity()  = QuartierEntity(id = this.quartierId, name = this.name)
