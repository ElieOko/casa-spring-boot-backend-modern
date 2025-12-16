package server.web.casa.app.actor.domain.model.join.master

import jakarta.validation.constraints.NotNull
import server.web.casa.app.actor.domain.model.request.PersonRequest
import server.web.casa.app.user.domain.model.UserRequest
import server.web.casa.app.user.domain.model.request.AccountRequest

data class PersonUserRequest(
    @NotNull
    val user : UserRequest,
    @NotNull
    val account : AccountRequest,
    @NotNull
    val actor : PersonRequest
)

data class PersonUserUpdateRequest(
    @NotNull
    val user : UserRequest,
    @NotNull
    val actor : PersonRequest
)