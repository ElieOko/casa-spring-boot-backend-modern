package server.web.casa.app.user.domain.model.request

import jakarta.validation.constraints.NotNull

data class AccountRequest(
    @NotNull
    val account : Long,
    @NotNull
    val typeAccount : Long
)
