package server.web.casa.app.actor.infrastructure.persistence.mapper

import server.web.casa.app.actor.domain.model.TypeCard
import server.web.casa.app.actor.infrastructure.persistence.entity.TypeCardEntity

fun TypeCardEntity.toDomain() = TypeCard(typeCardId = this.typeCardId, name = this.name)

fun TypeCard.toEntity() = TypeCardEntity(typeCardId = this.typeCardId, name = this.name)