package server.web.casa.app.property.domain.model.favorite

import jakarta.validation.constraints.NotNull
import server.web.casa.app.property.domain.model.Bureau
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteBureauEntity
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDateTime

class FavoriteBureau(
    val id              : Long,

    val userId          : Long,

    val bureauId        : Long,

    val createdAt       : LocalDateTime = LocalDateTime.now()
)
class FavoriteBureauDTO(
    val favorite    : FavoriteBureauEntity,
    val user        : UserDto,
    val bureau      : Bureau
)

class FavoriteBureauRequest(
    @NotNull
    val userId : Long,
    @NotNull
    val bureauId : Long
)