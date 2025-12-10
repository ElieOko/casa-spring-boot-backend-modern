package server.web.casa.app.ecosystem.domain.model.realisation

import server.web.casa.app.ecosystem.domain.model.task.AjusteurTask
import server.web.casa.app.ecosystem.domain.model.task.ElectricienTask
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.carreleur.ServiceCarreleurRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.electricien.ServiceElectricienRealisationEntity

data class ElectricienRealisation(
    val id : Long = 0,
    var service : ElectricienTask,
    val name : String = "",
    var path : String = ""
)

fun ElectricienRealisation.toEntity() = ServiceElectricienRealisationEntity(
    id = this.id,
    service = this.service.toEntity(),
    name = this.name,
    path = this.path
)