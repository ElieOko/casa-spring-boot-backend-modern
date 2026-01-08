package server.web.casa.app.pub.infrastructure.persistance.mapper

import server.web.casa.app.pub.domain.model.Publicity
import server.web.casa.app.pub.infrastructure.persistance.entity.PublicityEntity

fun PublicityEntity.toDomain() = Publicity(
    publicityId = this.id,
    user = this.user,
    image = this.imagePath,
    title = this.title,
    description = this.description,
    isActive = this.isActive,
    createdAt = this.createdAt
)

fun Publicity.toEntity() = PublicityEntity(
    id = this.publicityId,
    user = this.user,
    imagePath = this.image,
    title = this.title,
    description = this.description,
    isActive = this.isActive,
    createdAt = this.createdAt
)
