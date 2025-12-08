package server.web.casa.app.actor.infrastructure.persistence.mapper.master

import server.web.casa.app.actor.domain.model.Locataire
import server.web.casa.app.actor.infrastructure.persistence.entity.master.LocataireEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.user.infrastructure.persistence.mapper.*

fun LocataireEntity.toDomain() = Locataire(
    locataireId = this.locataireId,
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

fun Locataire.toEntity () = LocataireEntity(
    locataireId = this.locataireId,
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