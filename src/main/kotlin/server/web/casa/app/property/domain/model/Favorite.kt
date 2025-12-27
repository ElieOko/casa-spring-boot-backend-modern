package server.web.casa.app.property.domain.model

import server.web.casa.app.property.domain.model.dto.PropertyMasterDTO
import server.web.casa.app.property.infrastructure.persistence.entity.FavoriteEntity
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDate

data class Favorite(
    val favoriteId: Long? = null,
    val userId: Long?,
    val propertyId: Long?,
    val createdAt: LocalDate
)

data class FavoriteDTO(
    val favorite: FavoriteEntity?,
    val property: PropertyMasterDTO,

)