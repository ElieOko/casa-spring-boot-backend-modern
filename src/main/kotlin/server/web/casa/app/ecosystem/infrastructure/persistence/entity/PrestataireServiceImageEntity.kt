package server.web.casa.app.ecosystem.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.ecosystem.domain.model.realisation.AjusteurRealisation

@Table(name = "PrestataireServiceImages")
class PrestataireServiceImageEntity (
    @Id
    val id : Long = 0,
    @JsonBackReference
    var serviceId : Long,
    val name : String,
    val path : String
)

fun PrestataireServiceImageEntity.toDomain() = AjusteurRealisation(
    id = this.id,
    service = this.service.toDomain(),
    name = this.name,
    path = this.path
)