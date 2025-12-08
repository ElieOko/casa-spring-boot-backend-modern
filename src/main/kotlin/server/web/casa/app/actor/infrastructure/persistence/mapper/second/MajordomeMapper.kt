package server.web.casa.app.actor.infrastructure.persistence.mapper.second

import server.web.casa.app.actor.domain.model.Majordome
import server.web.casa.app.actor.infrastructure.persistence.entity.second.MajordomeEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.user.infrastructure.persistence.mapper.*

fun MajordomeEntity.toDomain() = Majordome(
    majordomeId = this.majordomeId,
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

fun Majordome.toEntity() = MajordomeEntity(
    majordomeId = this.majordomeId,
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