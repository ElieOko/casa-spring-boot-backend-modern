package server.web.casa.app.ecosystem.domain.model

import server.web.casa.app.ecosystem.infrastructure.persistence.entity.PrestationImageEntity

class PrestationImage(
    var id: Long? = null,
    val prestationId:Long,
    var name: String,
    var path: String = ""
)

fun PrestationImage.toEntity() = PrestationImageEntity(
    id = this.id,
    prestationId = this.prestationId,
    name = this.name,
    path = this.path
)