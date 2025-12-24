package server.web.casa.app.prestation.domain.request

import jakarta.validation.constraints.NotNull

data class CotationRequest(
    @NotNull
    val userId: Long,
    val sollicitationId: Long,
    val commentaire: String?,
    val cote: Float
)
