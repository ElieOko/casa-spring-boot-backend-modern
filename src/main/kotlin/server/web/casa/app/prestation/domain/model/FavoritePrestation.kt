package server.web.casa.app.prestation.domain.model

import java.time.LocalDateTime

data class FavoritePrestation(
    val favoritePrestationId: Long? = null,
    val userId: Long?,
    val prestationId: Long?,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
