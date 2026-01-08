package server.web.casa.app.property.domain.model.favorite

import server.web.casa.app.property.domain.model.dto.VacanceDTO
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteVacanceEntity
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDateTime

 class FavoriteVacance(
    val id          : Long,
    val userId      : Long,
    val vacanceId   : Long,
    val createdAt   : LocalDateTime = LocalDateTime.now()
)

 class FavoriteVacanceDTO(
    val favorite    : FavoriteVacanceEntity,
    val user        : UserDto,
   // val vacance     : VacanceDTO
)
