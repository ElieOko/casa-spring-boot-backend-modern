package server.web.casa.app.actor.infrastructure.persistence.mapper.master

import server.web.casa.app.actor.domain.model.Bailleur
import server.web.casa.app.actor.infrastructure.persistence.entity.master.BailleurEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.user.infrastructure.persistence.mapper.*

fun BailleurEntity.toDomain() = Bailleur(
    bailleurId = this.bailleurId,
    firstName = this.firstName,
    lastName = this.lastName,
    fullName = this.fullName,
    address = this.address,
    images = this.images,
    cardFront = this.cardFront,
    cardBack = this.cardBack,
    parrain = this.parrain?.toDomain(),
    user = this.user?.toDomain(),
    typeCard = this.typeCard?.toDomain(),
    numberCard = this.numberCard,
    note = this.note
)

fun Bailleur.toEntity() = BailleurEntity(
    bailleurId = this.bailleurId,
    firstName = this.firstName,
    lastName = this.lastName,
    fullName = this.fullName,
    address = this.address,
    images = this.images,
    cardFront = this.cardFront,
    cardBack = this.cardBack,
    parrain = this.parrain?.toEntityToDto(),
    user = this.user?.toEntityToDto(),
    typeCard = this.typeCard?.toEntity(),
    numberCard = this.numberCard,
    note = this.note
)
