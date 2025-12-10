package server.web.casa.app.ecosystem.domain.model.realisation

import server.web.casa.app.ecosystem.domain.model.task.SalubriteTask
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.salubrite.ServiceSalubriteRealisationEntity

data class SalubriteRealisation(
    val id : Long = 0,
    var service : SalubriteTask,
    val name : String = "",
    var path : String = ""
)
fun SalubriteRealisation.toEntity() = ServiceSalubriteRealisationEntity(
    id = this.id,
    service = this.service.toEntity(),
    name = this.name,
    path = this.path
)