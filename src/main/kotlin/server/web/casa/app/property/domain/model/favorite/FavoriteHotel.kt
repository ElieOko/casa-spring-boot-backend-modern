package server.web.casa.app.property.domain.model.favorite

import server.web.casa.app.property.domain.model.Hotel
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteHotelEntity
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDateTime

 class FavoriteHotel(
    val id          : Long,

    val userId      : Long,

    val hotelId     : Long,

    val createdAt   : LocalDateTime = LocalDateTime.now()
)
 class FavoriteHotelDTO(
    val favorite    : FavoriteHotelEntity,
    val user        : UserDto,
    //val hotel       : Hotel
)