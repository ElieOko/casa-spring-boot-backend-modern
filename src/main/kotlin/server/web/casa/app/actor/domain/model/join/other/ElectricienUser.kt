package server.web.casa.app.actor.domain.model.join.other

import jakarta.validation.constraints.NotNull
import server.web.casa.app.actor.domain.model.request.ElectricienRequest
import server.web.casa.app.user.domain.model.UserRequest

data class ElectricienUser(
    @NotNull
    val user : UserRequest,
    @NotNull
    val electricien : ElectricienRequest
)
