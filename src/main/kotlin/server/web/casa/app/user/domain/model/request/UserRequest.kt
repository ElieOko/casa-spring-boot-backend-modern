package server.web.casa.app.user.domain.model.request

import jakarta.validation.constraints.NotNull
import server.web.casa.app.address.domain.model.City

data class UserRequestChange(
    @NotNull
    val email : String,
    @NotNull
    val username : String,
    @NotNull
    val city  : City,
)
