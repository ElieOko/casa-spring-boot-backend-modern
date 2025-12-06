package server.web.casa.app.actor.domain.model.request

import jakarta.validation.constraints.NotNull

data class BrigoristeRequest(
    @NotNull
    val lastName: String,
    @NotNull
    val firstName: String,
    val address : String? = "",
//    @NotNull
    val images : String? = "",
//    @NotNull
    val cardFront : String? = "",
//    @NotNull
    val cardBack : String? = "",
    val typeCardId : Long? = 3,
//    @NotNull
    val numberCard : String? = null
)
