package server.web.casa.app.property.domain.model.favorite

import server.web.casa.app.property.domain.model.SalleFestive
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteFestiveEntity
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteFuneraireEntity
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDateTime

class FavoriteFestive(
    val id              : Long,

    val userId          : Long,

    val festiveId     : Long,

    val createdAt       : LocalDateTime = LocalDateTime.now()
)
data class FavoriteFestiveDTO(
    val favorite    : FavoriteFestiveEntity,
    val user        : UserDto,
    val salle       : SalleFestive
)