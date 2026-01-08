package server.web.casa.app.property.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import server.web.casa.app.property.infrastructure.persistence.entity.BureauImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.VacanceImageEntity
import java.time.LocalDateTime

data class BureauImage(
    @JsonIgnore
    val id: Long? = null,
    @JsonIgnore
    val bureauId : Long,
    val name: String = "",
    val path: String? = "",
    @JsonIgnore
    var isAvailable: Boolean = true,
    @JsonIgnore
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun BureauImage.toEntity()= BureauImageEntity(
    id = this.id,
    bureauId = this.bureauId,
    name = this.name,
    path = this.path,
    isAvailable = this.isAvailable,
    createdAt = this.createdAt,
)