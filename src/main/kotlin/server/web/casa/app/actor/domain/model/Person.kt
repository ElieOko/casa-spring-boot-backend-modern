package server.web.casa.app.actor.domain.model

import server.web.casa.app.actor.infrastructure.persistence.entity.PersonEntity

class Person(
    val id : Long? = 0,
    val firstName : String,
    val lastName : String,
    val fullName : String,
    val address : String? = "",
    val images : String? = null,
    val cardFront : String?,
    val cardBack : String? = null,
    val numberCard : String? = null,
    val userId : Long?,
    val parrainId : Long? = null,
    val typeCardId : Long? = null,
)

fun Person.toEntity() = PersonEntity(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    fullName = this.fullName,
    address = this.address,
    images = this.images,
    cardFront = this.cardFront,
    cardBack = this.cardBack,
    numberCard = this.numberCard,
    userId = this.userId,
    parrainId = this.parrainId,
    typeCardId = this.typeCardId,
)