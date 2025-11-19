package server.web.casa.app.user.domain.model.request

import jakarta.validation.constraints.*

data class UserRequestChange(
    @NotNull
    val email : String,
    @NotNull
    val username : String,
    @NotNull
    val city  : String,
)
