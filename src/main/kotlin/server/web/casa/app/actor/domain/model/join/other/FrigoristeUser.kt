package server.web.casa.app.actor.domain.model.join.other

import jakarta.validation.constraints.NotNull
import server.web.casa.app.actor.domain.model.request.FrigoristeRequest
import server.web.casa.app.user.domain.model.UserRequest

data class FrigoristeUser(
    @NotNull
    val user : UserRequest,
    @NotNull
    val frigoriste : FrigoristeRequest
)
