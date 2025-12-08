package server.web.casa.app.address.infrastructure.persistence.mapper

import server.web.casa.app.address.domain.model.Quartier
import server.web.casa.app.address.infrastructure.persistence.entity.QuartierEntity

fun QuartierEntity.toDomain() = Quartier(quartierId = this.quartierId, name = this.name)

fun Quartier.toEntity()  = QuartierEntity(quartierId = this.quartierId, name = this.name)
