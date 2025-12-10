package server.web.casa.app.payment.domain.model

import server.web.casa.app.payment.infrastructure.persistence.entity.DeviseEntity

data class Devise(
    val deviseId : Long,
    val name : String,
    val code : String,
    val tauxLocal : Double? = 0.0,
)

fun Devise.toEntity() = DeviseEntity(
    deviseId = this.deviseId,
    name = this.name,
    code = this.name,
    tauxLocal = this.tauxLocal
)
