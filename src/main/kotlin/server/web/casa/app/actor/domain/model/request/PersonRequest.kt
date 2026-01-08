package server.web.casa.app.actor.domain.model.request

import jakarta.validation.constraints.NotNull
import server.web.casa.app.user.domain.model.User
import server.web.casa.app.user.domain.model.UserUpdate

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
    val numberCard : String? = "",
)

data class PersonRequest2(
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
    val numberCard : String? = "",
    val user : UserUpdate,
)

data class PersonProfileRequest(
    @NotNull
    val images : String? = "",
    @NotNull
    val userId : Long,
)