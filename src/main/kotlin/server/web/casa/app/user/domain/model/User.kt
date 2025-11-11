package server.web.casa.app.user.domain.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import jakarta.validation.constraints.Size
import server.web.casa.app.address.domain.model.City
import java.time.LocalDateTime

data class User(
    @Null
    val userId        : Long = 0,
    @NotNull
    @field:NotBlank(message = "Le mot de passe est obligatoire")
    @field:Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    val password      : String,
    @NotNull
    val typeAccount   : TypeAccount? = null,
    @Null
    val email         : String? = null,
    @Null
    val username       : String? = null,
    @NotNull
    @field:NotBlank(message = "Ce numero est invalide")
    @field:Size(min = 8, message = "Ce numero est invalide car il ne respecte pas le nommage")
    val phone         : String,
    val city        : City? = null,
    @FutureOrPresent
    @Null
    val createdStart : LocalDateTime? = LocalDateTime.now()
)

data class UserRequest(
    @NotNull
    @field:NotBlank(message = "Le phone est obligatoire")
    @field:Size(min = 8, message = "Ce numero est invalide car il ne respecte pas le nommage")
    val phone : String,
    @NotNull
    @field:NotBlank(message = "Le mot de passe est obligatoire")
    @field:Size(min = 4, message = "Le mot de passe doit contenir au moins 4 caractères")
    val password : String,
    @NotNull
    val typeAccountId : Long,
    val email : String? = null,
    val username : String? = null,
    @NotNull
    val cityId : Long,
)
