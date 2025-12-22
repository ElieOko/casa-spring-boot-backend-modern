package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.property.domain.model.VacanceImage
import java.time.LocalDate
import java.time.LocalDateTime

@Table("vacance_images")
data class VacanceImageEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("vacance_id")
    val vacanceId : Long,
    @Column("name")
    val name: String,
    @Column("path")
    val path: String?,
    @Column("is_available")
    var isAvailable: Boolean = true,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun VacanceImage.toDomain() = VacanceImage(
    id = this.id,
    vacanceId = this.vacanceId,
    name = this.name,
    path = this.path,
    isAvailable = this.isAvailable,
    createdAt = this.createdAt
)