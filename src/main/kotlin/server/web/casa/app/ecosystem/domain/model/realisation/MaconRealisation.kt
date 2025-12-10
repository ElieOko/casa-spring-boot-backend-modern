package server.web.casa.app.ecosystem.domain.model.realisation

import server.web.casa.app.ecosystem.domain.model.task.AjusteurTask
import server.web.casa.app.ecosystem.domain.model.task.MaconTask
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.carreleur.ServiceCarreleurRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.macon.ServiceMaconRealisationEntity

data class MaconRealisation(
    val id : Long = 0,
    var service : MaconTask,
    val name : String = "",
    var path : String = ""
)

fun MaconRealisation.toEntity() = ServiceMaconRealisationEntity(
    id = this.id,
    service = this.service.toEntity(),
    name = this.name,
    path = this.path
)