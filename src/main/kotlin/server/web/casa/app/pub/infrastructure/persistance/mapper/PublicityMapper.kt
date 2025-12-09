package server.web.casa.app.pub.infrastructure.persistance.mapper

import server.web.casa.app.pub.domain.model.Publicity
import server.web.casa.app.pub.infrastructure.persistance.entity.PublicityEntity
import server.web.casa.app.user.infrastructure.persistence.mapper.*

fun PublicityEntity.toDomain() = Publicity(
    publicityId = this.publicityId,
    user = this.user?.toDomain(),
    image = this.image,
    title = this.title,
    description = this.description,
    isActive = this.isActive,
    createdAt = this.createdAt
)

fun Publicity.toEntity() = PublicityEntity(
    publicityId = this.publicityId,
    user = this.user?.toEntityToDto(),
    image = this.image,
    title = this.title,
    description = this.description,
    isActive = this.isActive,
    createdAt = this.createdAt
)
