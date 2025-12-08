package server.web.casa.app.actor.infrastructure.persistence.mapper.other

import server.web.casa.app.actor.domain.model.Frigoriste
import server.web.casa.app.actor.infrastructure.persistence.entity.other.FrigoristeEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.user.infrastructure.persistence.mapper.*

fun FrigoristeEntity.toDomain() = Frigoriste(
    frigoristeId = this.frigoristeId,
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

fun Frigoriste.toEntity() = FrigoristeEntity(
    frigoristeId = this.frigoristeId,
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