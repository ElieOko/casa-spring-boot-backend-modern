package server.web.casa.app.actor.domain.model.join.other

import jakarta.validation.constraints.NotNull
import server.web.casa.app.actor.domain.model.request.PeintreRequest
import server.web.casa.app.user.domain.model.UserRequest

data class PeintreUser(
    @NotNull
    val user : UserRequest,
    @NotNull
    val peintre : PeintreRequest
)
