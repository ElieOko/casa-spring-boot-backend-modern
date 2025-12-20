package server.web.casa.app.ecosystem.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.*
import server.web.casa.app.ecosystem.domain.model.PrestationImage

@Table(name = "prestation_images")
class PrestationImageEntity (
    @Id
    @Column("id")
    val id : Long? = null,
    @JsonBackReference
    @Column("prestation_id")
    var prestationId : Long,
    @Column("name")
    val name : String,
    @Column("path")
    val path : String
)
fun PrestationImageEntity.toDomain() = PrestationImage(
    id = this.id,
    prestationId = this.prestationId,
    name = this.name,
    path = this.path
)