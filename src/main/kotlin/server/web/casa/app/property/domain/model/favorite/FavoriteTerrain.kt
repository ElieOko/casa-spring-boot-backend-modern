package server.web.casa.app.property.domain.model.favorite

import server.web.casa.app.property.domain.model.Terrain
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteTerrainEntity
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDateTime

data class FavoriteTerrain(
    val id          : Long,
    val userId      : Long,
    val terrainId   : Long,
    val createdAt   : LocalDateTime = LocalDateTime.now()
)

data class FavoriteTerrainDTO(
    val favorite    : FavoriteTerrainEntity,
    val user        : UserDto,
    val terrain     : Terrain
)