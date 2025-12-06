package server.web.casa.app.actor.domain.model.join.other

import jakarta.validation.constraints.NotNull
import server.web.casa.app.actor.domain.model.request.ChauffeurRequest
import server.web.casa.app.user.domain.model.UserRequest

data class ChauffeurUser(
    @NotNull
    val user : UserRequest,
    @NotNull
    val chauffeur : ChauffeurRequest
)
