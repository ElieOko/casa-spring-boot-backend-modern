package server.web.casa.app.actor.infrastructure.persistence.mapper.other

import server.web.casa.app.actor.domain.model.Architect
import server.web.casa.app.actor.infrastructure.persistence.entity.other.*
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.user.infrastructure.persistence.mapper.*

fun ArchitecteEntity.toDomain() = Architect(
    architectId = this.architectId,
    firstName = this.firstName,
    lastName = this.lastName,
    fullName = this.fullName,
    address = this.address,
    images = this.images,
    cardFront = this.cardFront,
    cardBack = this.cardBack,
    user = this.user?.toDomain(),
    typeCard = this.typeCard?.toDomain(),
    numberCard = this.numberCard
)

fun Architect.toEntity() = ArchitecteEntity(
    architectId = this.architectId,
    firstName = this.firstName,
    lastName = this.lastName,
    fullName = this.fullName,
    address = this.address,
    images = this.images,
    cardFront = this.cardFront,
    cardBack = this.cardBack,
    user = this.user?.toEntityToDto(),
    typeCard = this.typeCard?.toEntity(),
    numberCard = this.numberCard
)