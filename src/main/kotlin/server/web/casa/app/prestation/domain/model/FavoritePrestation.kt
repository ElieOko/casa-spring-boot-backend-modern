package server.web.casa.app.prestation.domain.model

import server.web.casa.app.ecosystem.domain.model.Prestation
import server.web.casa.app.prestation.infrastructure.persistance.entity.FavoritePrestationEntity
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDateTime

data class FavoritePrestation(
    val favoritePrestationId: Long? = null,
    val userId: Long?,
    val prestationId: Long?,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

data class FavoritePrestationDTO(
    val favorite : FavoritePrestationEntity,
    val user : UserDto,
    val prestation: Prestation
)