package server.web.casa.app.ecosystem.domain.model.realisation

import server.web.casa.app.ecosystem.domain.model.task.AjusteurTask
import server.web.casa.app.ecosystem.domain.model.task.PlombierTask
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.peintre.ServicePeintreRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.plombier.ServicePlombierRealisationEntity

data class PlombierRealisation(
    val id : Long = 0,
    var service : PlombierTask,
    val name : String = "",
    var path : String = ""
)
fun PlombierRealisation.toEntity() = ServicePlombierRealisationEntity(
    id = this.id,
    service = this.service.toEntity(),
    name = this.name,
    path = this.path
)