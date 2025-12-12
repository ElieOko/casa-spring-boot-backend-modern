package server.web.casa.app.user.domain.model

import jakarta.validation.constraints.*
import java.time.LocalDateTime

data class User(
    @Null
    val userId: Long = 0,
    @NotNull
    @field:NotBlank(message = "Le mot de passe est obligatoire")
    @field:Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    val password: String ="",
    @NotNull
    val accountId: Long? = null,
    @Null
    val email: String? = null,
    @Null
    val username: String? = null,
    val phone: String? = null,
    val city: String = "",
    @NotNull
    val country : String,
    @FutureOrPresent
    @Null
    val createdStart: LocalDateTime? = LocalDateTime.now()
)

data class UserDto(
    val userId: Long = 0,
    val accountId: Long? = null,
    val email: String? = null,
    val username: String,
    val phone: String,
    val city: String,
    val country : String? = "Democratic Republic of the Congo",
    val isPremium : Boolean,
    val isCertified: Boolean
)

data class UserRequest(
    val phone : String? = null,
    @NotNull
    @field:NotBlank(message = "Le mot de passe est obligatoire")
    @field:Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    val password : String,
    @NotNull
    val accountId : Long,
    val email : String? = null,
    val username : String? = null,
    @NotNull
    val city : String,
    @NotNull
    val country : String
)

data class RefreshRequest(
    val refreshToken: String
)