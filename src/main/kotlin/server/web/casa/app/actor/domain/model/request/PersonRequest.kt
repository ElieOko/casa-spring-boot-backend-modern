package server.web.casa.app.actor.domain.model.request

import jakarta.validation.constraints.NotNull

data class PersonRequest(
    @NotNull
    val lastName: String,
    @NotNull
    val firstName: String,
    val address : String? = "",
    val images : String? = "",
    val cardFront : String? = "",
    val cardBack : String? ="",
    val typeCardId : Long? = 0,
    val parrainId : Long? = 0,
    val numberCard : String? = ""
)
