package server.web.casa.app.property.domain.model.favorite

import server.web.casa.app.property.domain.model.SalleFuneraire
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteFuneraireEntity
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDateTime

class FavoriteFuneraire(
    val id              : Long,

    val userId          : Long,

    val funeraireId     : Long,

    val createdAt       : LocalDateTime = LocalDateTime.now()
)
 class FavoriteFuneraireDTO(
     val favorite    : FavoriteFuneraireEntity,
     val user        : UserDto,
     val salle       : SalleFuneraire
)