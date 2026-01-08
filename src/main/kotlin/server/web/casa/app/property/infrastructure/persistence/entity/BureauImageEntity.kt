package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.property.domain.model.Bureau
import server.web.casa.app.property.domain.model.BureauImage
import server.web.casa.app.property.domain.model.VacanceImage
import java.time.LocalDate
import java.time.LocalDateTime

@Table("bureau_images")
data class BureauImageEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("bureau_id")
    val bureauId : Long,
    @Column("name")
    var name: String,
    @Column("path")
    var path: String?,
    @Column("is_available")
    var isAvailable: Boolean = true,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
fun BureauImageEntity.toDomain() = BureauImage(
    id = this.id,
    bureauId = this.bureauId,
    name = this.name,
    path = this.path,
    isAvailable = this.isAvailable,
    createdAt = this.createdAt,
)
