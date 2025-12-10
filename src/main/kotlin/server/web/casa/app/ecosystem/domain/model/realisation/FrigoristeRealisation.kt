package server.web.casa.app.ecosystem.domain.model.realisation

import server.web.casa.app.ecosystem.domain.model.task.AjusteurTask
import server.web.casa.app.ecosystem.domain.model.task.FrigoristeTask
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.carreleur.ServiceCarreleurRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.frigoriste.ServiceFrigoristeRealisationEntity

data class FrigoristeRealisation(
    val id : Long = 0,
    var service : FrigoristeTask,
    val name : String = "",
    var path : String =""
)

fun FrigoristeRealisation.toEntity() = ServiceFrigoristeRealisationEntity(
    id = this.id,
    service = this.service.toEntity(),
    name = this.name,
    path = this.path
)