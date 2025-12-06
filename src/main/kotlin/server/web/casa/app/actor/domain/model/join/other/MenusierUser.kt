package server.web.casa.app.actor.domain.model.join.other

import jakarta.validation.constraints.NotNull
import server.web.casa.app.actor.domain.model.request.MenusierRequest
import server.web.casa.app.user.domain.model.UserRequest

data class MenusierUser(
    @NotNull
    val user : UserRequest,
    @NotNull
    val menusier : MenusierRequest
)
