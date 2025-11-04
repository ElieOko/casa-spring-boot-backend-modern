package server.web.casa.app.property.domain.model

import server.web.casa.app.user.domain.model.User
import java.time.LocalDate

data class Favorite(
    val favoriteId: Long = 0,
    val user: User?,
    val property: Property?,
    val createdAt: LocalDate
)
