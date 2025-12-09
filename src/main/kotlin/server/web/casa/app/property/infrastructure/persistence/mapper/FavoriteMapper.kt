package server.web.casa.app.property.infrastructure.persistence.mapper

import server.web.casa.app.property.domain.model.Favorite
import server.web.casa.app.property.infrastructure.persistence.entity.FavoriteEntity
import server.web.casa.app.user.infrastructure.persistence.mapper.toDomain
import server.web.casa.app.user.infrastructure.persistence.mapper.toEntityToDto

fun FavoriteEntity.toDomain() =  Favorite(
    favoriteId = this.favoriteId,
    user = this.user.toDomain(),
    property = this.property!!.toDomain(),
    createdAt = this.createdAt
)

fun Favorite.toEntity() = FavoriteEntity(
    favoriteId = this.favoriteId,
    user = this.user!!.toEntityToDto(),
    property = this.property!!.toEntity(),
    createdAt = this.createdAt
)