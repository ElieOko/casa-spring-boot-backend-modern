package server.web.casa.app.property.infrastructure.persistence.mapper

import server.web.casa.app.property.domain.model.Favorite
import server.web.casa.app.property.infrastructure.persistence.entity.FavoriteEntity

fun FavoriteEntity.toDomain() =  Favorite(
    favoriteId = this.id,
    userId = this.userId,
    propertyId = this.propertyId,
    createdAt = this.createdAt
)

fun Favorite.toEntity() = FavoriteEntity(
    id = this.favoriteId,
    userId = this.userId,
    propertyId = this.propertyId,
    createdAt = this.createdAt
)