package server.web.casa.app.actor.infrastructure.persistence.mapper.other

import server.web.casa.app.actor.domain.model.Electricien
import server.web.casa.app.actor.infrastructure.persistence.entity.other.ElectricienEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.toDomain
import server.web.casa.app.actor.infrastructure.persistence.mapper.toEntity
import server.web.casa.app.user.infrastructure.persistence.mapper.toDomain
import server.web.casa.app.user.infrastructure.persistence.mapper.toEntityToDto

fun ElectricienEntity.toDomain() = Electricien(
    electricienId = this.electricienId,
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

fun Electricien.toEntity() = ElectricienEntity(
    electricienId = this.electricienId,
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