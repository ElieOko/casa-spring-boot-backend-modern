package server.web.casa.app.ecosystem.domain.model.realisation

import server.web.casa.app.ecosystem.domain.model.task.ArchitectTask
import server.web.casa.app.ecosystem.domain.model.task.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.ajusteur.ServiceAjusteurRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.architect.ServiceArchitectRealisationEntity

data class ArchitectRealisation(
    val id : Long = 0,
    var service : ArchitectTask,
    val name : String = "",
    var path : String = ""
)

fun ArchitectRealisation.toEntity() = ServiceArchitectRealisationEntity(
    id = this.id,
    service = this.service.toEntity(),
    name = this.name,
    path = this.path
)