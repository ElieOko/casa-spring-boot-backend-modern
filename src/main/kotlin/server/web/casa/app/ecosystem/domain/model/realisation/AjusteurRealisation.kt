package server.web.casa.app.ecosystem.domain.model.realisation

import server.web.casa.app.ecosystem.domain.model.task.AjusteurTask
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.ServiceAjusteurRealisationEntity


data class AjusteurRealisation(
    val id : Long = 0,
    var service : AjusteurTask,
    val name : String = "",
    var path : String = ""
)

fun AjusteurRealisation.toEntity() = ServiceAjusteurRealisationEntity(
    id = this.id,
    service = this.service.toEntity(),
    name = this.name,
    path = this.path
)