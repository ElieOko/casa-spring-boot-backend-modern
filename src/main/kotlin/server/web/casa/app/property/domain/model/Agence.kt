package server.web.casa.app.property.domain.model

import server.web.casa.app.property.infrastructure.persistence.entity.AgenceEntity

class Agence(
    val id: Long? = null,
    val userId: Long,
    val name: String,
    val description : String = "",
    val phone1 : String = "",
    val phone2 : String = "",
    val address : String = "",
    var logo : String? = "",
)

fun Agence.toEntity() = AgenceEntity(
    this.id,
    this.userId,
    this.name,
    this.description,
    this.phone1,
    this.phone2,
    this.address,
    this.logo
)