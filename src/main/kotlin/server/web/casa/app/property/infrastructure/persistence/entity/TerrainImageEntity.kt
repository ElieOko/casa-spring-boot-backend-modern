package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.property.domain.model.SalleFestiveImage
import server.web.casa.app.property.domain.model.TerrainImage
import java.time.LocalDateTime

@Table("terrain_images")
data class TerrainImageEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("terrain_id")
    val terrainId : Long,
    @Column("name")
    var name: String,
    @Column("path")
    var path: String?,
    @Column("is_available")
    var isAvailable: Boolean = true,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun TerrainImageEntity.toDomain()= TerrainImage(
    id = this.id,
    name = this.name,
    path = this.path,
    isAvailable = this.isAvailable,
    createdAt = this.createdAt,
    terrainId = this.terrainId
)