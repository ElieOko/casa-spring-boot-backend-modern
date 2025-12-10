package server.web.casa.app.ecosystem.domain.model.realisation

import server.web.casa.app.ecosystem.domain.model.task.AjusteurTask
import server.web.casa.app.ecosystem.domain.model.task.PeintreTask
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.menusier.ServiceMenusierRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.peintre.ServicePeintreRealisationEntity

data class PeintreRealisation(
    val id : Long = 0,
    var service : PeintreTask,
    val name : String = "",
    var path : String = ""
)

fun PeintreRealisation.toEntity() = ServicePeintreRealisationEntity(
    id = this.id,
    service = this.service.toEntity(),
    name = this.name,
    path = this.path
)