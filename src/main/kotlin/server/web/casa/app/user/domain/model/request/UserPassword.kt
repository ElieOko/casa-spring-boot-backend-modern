package server.web.casa.app.user.domain.model.request

import jakarta.validation.constraints.*

data class UserPassword(
    @NotNull
    @field:NotBlank(message = "L'ancien mot de passe est obligatoire")
    @field:Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    val oldPassword : String,
    @NotNull
    @field:NotBlank(message = "Le nouveau mot de passe est obligatoire")
    @field:Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    val newPassword : String
)
