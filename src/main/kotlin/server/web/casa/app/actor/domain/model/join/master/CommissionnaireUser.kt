package server.web.casa.app.actor.domain.model.join.master

import jakarta.validation.constraints.NotNull
import server.web.casa.app.actor.domain.model.request.CommissionnaireRequest
import server.web.casa.app.user.domain.model.UserRequest

data class CommissionnaireUser(
    @NotNull
    val user : UserRequest,
    @NotNull
    val commissionnaire: CommissionnaireRequest
)