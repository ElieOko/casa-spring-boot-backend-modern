package server.web.casa.app.property.domain.model

import server.web.casa.app.property.infrastructure.persistence.entity.SalleFestiveImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.TerrainImageEntity
import java.time.LocalDateTime

data class SalleFestiveImage(
    val id: Long? = null,
    val salleFestiveId : Long,
    val name: String,
    val path: String?,
    var isAvailable: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun SalleFestiveImage.toEntity()= SalleFestiveImageEntity(
    id = this.id,
    name = this.name,
    path = this.path,
    isAvailable = this.isAvailable,
    createdAt = this.createdAt,
    salleFestiveId = this.salleFestiveId
)

data class TerrainImage(
    val id: Long? = null,
    val terrainId : Long,
    val name: String,
    val path: String?,
    var isAvailable: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
fun TerrainImage.toEntity()= TerrainImageEntity(
    id = this.id,
    name = this.name,
    path = this.path,
    isAvailable = this.isAvailable,
    createdAt = this.createdAt,
    terrainId = this.terrainId
)