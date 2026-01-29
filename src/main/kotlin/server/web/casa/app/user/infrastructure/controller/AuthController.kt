package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*
import server.web.casa.app.user.application.service.AuthService
import server.web.casa.app.user.application.service.TypeAccountService
import server.web.casa.app.user.domain.model.RefreshRequest
import server.web.casa.app.user.domain.model.UserAuth
import server.web.casa.app.user.domain.model.UserAuthRequest
import server.web.casa.app.user.domain.model.request.IdentifiantRequest
import server.web.casa.app.user.domain.model.request.UserPassword
import server.web.casa.app.user.domain.model.request.VerifyRequest
import server.web.casa.app.user.domain.model.toDomain
import server.web.casa.route.auth.AuthRoute
import server.web.casa.security.Auth

const val ROUTE_REGISTER = AuthRoute.REGISTER
const val ROUTE_LOGIN = AuthRoute.LOGIN

@Tag(name = "Authentification", description = "Gestion des accès")
@RestController
@RequestMapping
@Profile("dev")
class AuthController(
    private val authService: AuthService,
    private val accountService: TypeAccountService,
    private val auth: Auth,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    @Operation(summary = "Création utilisateur")
    @PostMapping(ROUTE_REGISTER)
    suspend fun register(
        @Valid @RequestBody request : UserAuthRequest
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val accountItems = request.account
        if (accountItems.isNotEmpty()){
          val account = accountItems.map { accountService.findByIdTypeAccount(it.typeAccount) }.first()
          val userSystem = request.toDomain()
          val data = authService.register(userSystem,accountItems)
          val response = mapOf("user" to data.first, "token" to data.second, "message" to "Votre compte principal ${account.name} a été créer avec succès")
          ResponseEntity.status(201).body(response)
        }
        val response = mapOf("message" to "Vous devez selectionner au moins un compte pour vous enregistrez")
        ResponseEntity.status(404).body(response)
    }

    @Operation(summary = "Connexion utilisateur")
    @PostMapping(ROUTE_LOGIN)
    suspend fun login(
      @Valid @RequestBody body: UserAuth
    ): ResponseEntity<Map<String, Any?>>  = coroutineScope {
      val data = authService.login(body.identifiant, body.password)
        try {
            val response = mapOf("member" to data.second, "token" to data.first.accessToken, "refresh_token" to data.first.refreshToken, "message" to "Connexion réussie avec succès")
            ResponseEntity.ok().body(response)
        }
        catch (e: AuthenticationException){
            log.info(e.message)
            val response = mapOf("message" to e.message)
            ResponseEntity.status(401).body(response)
        }
    }

    @PostMapping("/api/{version}/protected/token/refresh")
    suspend fun refresh(@RequestBody body: RefreshRequest): AuthService.TokenPair = coroutineScope { authService.refresh(body.refreshToken) }
    @Operation(summary = "OTP activation send code")
    @PostMapping("/api/{version}/public/otp/generate")
    suspend fun generateKeyOTP(
        @RequestBody @Valid user : IdentifiantRequest
    ): ResponseEntity<Map<String, String?>> = coroutineScope {
       val result = authService.generateOTP(user.identifier)
       val message = mapOf("message" to result.second, "status" to result.first, "phone" to result.third)
       ResponseEntity.ok(message)
    }

    @Operation(summary = "OTP activation send code")
    @PostMapping("/api/{version}/public/otp/verify")
    suspend fun verifyOTP(
        @RequestBody @Valid user : VerifyRequest
    ): ResponseEntity<out Map<String, Any?>> = coroutineScope {
        val result = authService.verifyOTP(user)
        val message = mapOf("status" to result.second, "user" to result.first)
        ResponseEntity.ok(message)
    }

    @Operation(summary = "Reset password ")
    @PutMapping("/api/{version}/protected/reset/password")
    suspend fun resetPassword(
        @RequestBody @Valid user : UserPassword
    ) : ResponseEntity<Map<String, String>> = coroutineScope {
       val new = user.newPassword
       authService.changePassword(user.userId,new)
       val message = mapOf("message" to "Mot de passe changé avec succès")
       ResponseEntity.ok(message)
    }

    @Operation(summary = "Change password utilisateur")
    @PutMapping("/api/{version}/protected/change/password")
    suspend fun updateUser(
        @RequestBody @Valid user : UserPassword
    ) : ResponseEntity<Map<String, String>> = coroutineScope {
        val userConnect = auth.user()
        val new = user.newPassword
//        val old = user.oldPassword
        authService.changePassword(userConnect?.first?.userId!!,new)
        val message = mapOf("message" to "Mot de passe changé avec succès")
        ResponseEntity.ok(message)
    }

    @Operation(summary = "Delete Account User")
    @DeleteMapping("/api/{version}/protected/users/delete/user")
    suspend fun lockAccount(): ResponseEntity<Map<String, String>> = coroutineScope {
        val userId = auth.user()?.first?.userId
        if (userId == null) ResponseEntity.status(404).body(mapOf("message" to "User not found"))
        val state = authService.lockedOrUnlocked(userId as Long)
        val message = mapOf("message" to if (state) "Votre compte a été supprimé avec succès" else "Cet utilisateur n'existe pas")
        ResponseEntity.ok(message)
    }
    @Operation(summary = "Recovery Account User")
    @PutMapping("/api/{version}/protected/recovery/user/{id}")
    suspend fun unlockAccount(@PathVariable("id") id : Long): ResponseEntity<Map<String, String>> = coroutineScope {
        val state = authService.lockedOrUnlocked(id,false)
        val message = mapOf("message" to if (state) "Votre compte a été restauré avec succès" else "Cet utilisateur n'existe pas")
        ResponseEntity.ok(message)
    }
}