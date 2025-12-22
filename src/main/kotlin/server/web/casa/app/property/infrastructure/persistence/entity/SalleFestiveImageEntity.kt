package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("salle_festive_images")
data class SalleFestiveImageEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("salle_festive_id")
    val salleFestiveId : Long,
    @Column("name")
    val name: String,
    @Column("path")
    val path: String?,
    @Column("is_available")
    var isAvailable: Boolean = true,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
