package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*
import server.web.casa.app.actor.application.service.*
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
    private val commissionnaire : CommissionnaireService,
    private val locataire : LocataireService,
    private val bailleur : BailleurService,
    private val typeAccountService: TypeAccountService,
    private val auth: Auth,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    @Operation(summary = "Création utilisateur")
    @PostMapping(ROUTE_REGISTER)
    suspend fun register(
        @Valid @RequestBody user: UserRequest
    ): ResponseEntity<Map<String, Any?>> {
        if (user.typeAccountId == 0L){
            val response = mapOf(
                "message" to "0 n'est ne peut pas être identifiant"
            )
           return ResponseEntity.status(404).body(response)
        }
        if (user.typeAccountId > 4L){
            val response = mapOf(
                "message" to "Ce type de compte n'existe pas"
            )
            return ResponseEntity.status(404).body(response)
        }
        val city = user.city
        val typeAccount = typeAccountService.findByIdTypeAccount(user.typeAccountId)
        if (typeAccount != null){
            val userSystem = User(
                password = user.password,
                typeAccount = typeAccount,
                email = user.email,
                username ="@"+user.username,
                phone = user.phone,
                city = city,
                country = user.country
            )
           val data = authService.register(userSystem)
           val response = mapOf(
               "user" to data.first,
               "token" to data.second,
               "message" to "Votre compte ${data.first?.typeAccount?.name} a été créer avec succès"
            )
           return ResponseEntity.status(201).body(response)
        }
        val response = mapOf("error" to "Erreur au niveau de la validation des données")
        return ResponseEntity.badRequest().body(response)
    }

    @Operation(summary = "Connexion utilisateur")
    @PostMapping(ROUTE_LOGIN)
    suspend fun login(
      @Valid @RequestBody body: UserAuth
    ): ResponseEntity<Map<String, Any?>> {
      val data = authService.login(body.identifiant, body.password)
      var profile : ProfileUser? = null
          when(data.second?.typeAccount?.typeAccountId){
              1L -> {}
              2L->{
                val actor = commissionnaire.findAllCommissionnaire().filter{it.user?.userId == data.second?.userId}[0]
                 profile = ProfileUser(
                     firstname = actor.firstName,
                     lastname = actor.lastName,
                     fullname = actor.fullName,
                     address = actor.address,
                     images = actor.images,
                     cardFront = actor.cardFront,
                     cardBack = actor.cardBack,
                     numberCard = actor.numberCard
                 )
              }
              3L-> {
                  val actor = bailleur.findAllBailleur().filter{it.user?.userId == data.second?.userId }[0]
                  profile = ProfileUser(
                      firstname = actor.firstName,
                      lastname = actor.lastName,
                      fullname = actor.fullName,
                      address = actor.address,
                      images = actor.images,
                      cardFront = actor.cardFront,
                      cardBack = actor.cardBack,
                      numberCard = actor.numberCard
                  )
              }
              4L-> {
                  val actor = locataire.findAllLocataire().filter{ it.user?.userId == data.second?.userId }[0]
                  profile = ProfileUser(
                      firstname = actor.firstName,
                      lastname = actor.lastName,
                      fullname = actor.fullName,
                      address = actor.address,
                      images = actor.images,
                      cardFront = actor.cardFront,
                      cardBack = actor.cardBack,
                      numberCard = actor.numberCard
                  )
              }
              else -> {}
          }
        try {
                val response = mapOf(
                    "user" to data.second,
                    "profile" to profile,
                    "token" to data.first.accessToken,
                    "refresh_token" to data.first.refreshToken,
                    "message" to "Connexion réussie avec succès",
                )
                return ResponseEntity.ok().body(response)
        }
        catch (e: AuthenticationException){
            log.info(e.message)
        }
        val response = mapOf(
            "error" to ""
        )
        return ResponseEntity.ok().body(response)
    }

    @PostMapping("/refresh")
    suspend fun refresh(
        @RequestBody body: RefreshRequest
    ): AuthService.TokenPair {
        return authService.refresh(body.refreshToken)
    }

    @Operation(summary = "OTP activation send code")
    @PostMapping("/otp/generate")
    fun generateKeyOTP(
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
    fun verifyOTP(
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
        authService.changePassword(userConnect!!.userId,new)
        val message = mapOf(
            "message" to "Mot de passe changé avec succès"
        )
        return ResponseEntity.ok(message)
    }
}