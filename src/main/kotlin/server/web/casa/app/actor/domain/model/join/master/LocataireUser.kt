package server.web.casa.app.actor.domain.model.join.master

import jakarta.validation.constraints.NotNull
import server.web.casa.app.actor.domain.model.request.LocataireRequest
import server.web.casa.app.user.domain.model.UserRequest

data class LocataireUser(
    @NotNull
    val user : UserRequest,
    @NotNull
    val locataire: LocataireRequest
)
