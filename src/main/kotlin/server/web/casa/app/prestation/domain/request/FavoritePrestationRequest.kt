package server.web.casa.app.prestation.domain.request

import org.jetbrains.annotations.NotNull

data class FavoritePrestationRequest(
    @NotNull
    val userId: Long,
    @NotNull
    val prestationId: Long
)
