package server.web.casa.app.property.domain.model

import server.web.casa.app.property.infrastructure.persistence.entity.VacanceImageEntity
import java.time.LocalDateTime

data class VacanceImage(
    val id: Long? = null,
    val vacanceId : Long,
    val name: String = "",
    val path: String = "",
    var isAvailable: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun VacanceImage.toEntity()= VacanceImageEntity(
    id = this.id,
    vacanceId = this.vacanceId,
    name = this.name,
    path = this.path,
    isAvailable = this.isAvailable,
    createdAt = this.createdAt,
)