package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*
import server.web.casa.app.actor.application.service.PersonService
import server.web.casa.app.user.application.service.*
import server.web.casa.app.user.domain.model.*
import server.web.casa.app.user.domain.model.request.IdentifiantRequest
import server.web.casa.app.user.domain.model.request.UserPassword
import server.web.casa.app.user.domain.model.request.VerifyRequest
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
    private val servicePerson: PersonService,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    @Operation(summary = "Création utilisateur")
    @PostMapping(ROUTE_REGISTER)
    suspend fun register(
        @Valid @RequestBody request : UserAuthRequest
    ): ResponseEntity<Map<String, Any?>> {
        val accountItems = request.account
        if (accountItems.isNotEmpty()){
          val account = accountItems.map { accountService.findByIdTypeAccount(it.typeAccount) }.first()
          val userSystem = request.toDomain()
          val data = authService.register(userSystem,accountItems)
          val response = mapOf(
                "user" to data.first,
                "token" to data.second,
                "message" to "Votre compte principal ${account.name} a été créer avec succès"
            )
          return ResponseEntity.status(201).body(response)
        }
        else{
            throw Exception()
        }
    }

    @Operation(summary = "Connexion utilisateur")
    @PostMapping(ROUTE_LOGIN)
    suspend fun login(
      @Valid @RequestBody body: UserAuth
    ): ResponseEntity<Map<String, Any?>> {
      val data = authService.login(body.identifiant, body.password)
        try {
            val response = mapOf("member" to data.second, "token" to data.first.accessToken, "refresh_token" to data.first.refreshToken, "message" to "Connexion réussie avec succès")
            return ResponseEntity.ok().body(response)
        }
        catch (e: AuthenticationException){
            log.info(e.message)
            val response = mapOf("error" to "")
            return ResponseEntity.ok().body(response)
        }
    }

    @PostMapping("/refresh")
    suspend fun refresh(
        @RequestBody body: RefreshRequest
    ): AuthService.TokenPair {
        return authService.refresh(body.refreshToken)
    }

    @Operation(summary = "OTP activation send code")
    @PostMapping("/otp/generate")
    suspend fun generateKeyOTP(
        @RequestBody @Valid user : IdentifiantRequest
    ): ResponseEntity<Map<String, String?>> {
       val result = authService.generateOTP(user.identifier)
        val message = mapOf(
            "message" to result.second,
            "status" to result.first,
            "phone" to result.third,
        )
        return ResponseEntity.ok(message)
    }

    @Operation(summary = "OTP activation send code")
    @PostMapping("/otp/verify")
    suspend fun verifyOTP(
        @RequestBody @Valid user : VerifyRequest
    ): ResponseEntity<out Map<String, Any?>> {
        val result = authService.verifyOTP(user)
        val message = mapOf(
            "status" to result.second,
            "user" to result.first
        )
        return ResponseEntity.ok(message)
    }

    @Operation(summary = "Reset password ")
    @PutMapping("/reset/password")
    suspend fun resetPassword(
        @RequestBody @Valid user : UserPassword
    ) : ResponseEntity<Map<String, String>> {
        val new = user.newPassword
        authService.changePassword(user.userId,new)
        val message = mapOf(
            "message" to "Mot de passe changé avec succès"
        )
        return ResponseEntity.ok(message)
    }

    @Operation(summary = "Change password utilisateur")
    @PutMapping("/change/password")
    suspend fun updateUser(
        @RequestBody @Valid user : UserPassword
    ) : ResponseEntity<Map<String, String>> {
        val userConnect = auth.user()
        val new = user.newPassword
//        val old = user.oldPassword
        authService.changePassword(userConnect?.userId!!,new)
        val message = mapOf(
            "message" to "Mot de passe changé avec succès"
        )
        return ResponseEntity.ok(message)
    }
}