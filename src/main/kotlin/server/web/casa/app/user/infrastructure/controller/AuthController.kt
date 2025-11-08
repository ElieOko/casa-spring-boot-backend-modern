package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*
import server.web.casa.app.actor.application.service.BailleurService
import server.web.casa.app.actor.application.service.CommissionnaireService
import server.web.casa.app.actor.application.service.LocataireService
import server.web.casa.app.address.application.service.CityService
import server.web.casa.app.user.application.AuthService
import server.web.casa.app.user.application.TypeAccountService
import server.web.casa.app.user.domain.model.ProfileUser
import server.web.casa.app.user.domain.model.User
import server.web.casa.app.user.domain.model.UserAuth
import server.web.casa.app.user.domain.model.UserRequest
import server.web.casa.app.user.domain.model.request.UserPassword
import server.web.casa.route.auth.AuthRoute
import server.web.casa.security.Auth
import server.web.casa.security.HashEncoder

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
    private val cityService: CityService,
    private val typeAccountService: TypeAccountService,
    private val auth: Auth,
    private val hashEncoder: HashEncoder,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    @Operation(summary = "Création utilisateur")
    @PostMapping(ROUTE_REGISTER)
    suspend fun register(
        @Valid @RequestBody user: UserRequest
    ): ResponseEntity<Map<String, Any?>> {
        if (user.cityId == 0L || user.typeAccountId == 0L){
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
        val city = cityService.findByIdCity(user.cityId)
        val typeAccount = typeAccountService.findByIdTypeAccount(user.typeAccountId)
        if (city != null && typeAccount != null){
            val userSystem = User(
                password = user.password,
                typeAccount = typeAccount,
                email = user.email,
                phone = user.phone,
                city = city
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

    @Operation(summary = "Change password utilisateur")
    @PutMapping("/change/password")
    suspend fun updateUser(
        @RequestBody @Valid user : UserPassword
    ) : ResponseEntity<Map<String, String>> {
        val userConnect = auth.user()
        val new = user.newPassword
        val old = user.oldPassword
        val updated = authService.changePassword(userConnect!!.userId,new,old)
        val message = mapOf(
            "message" to "Mot de passe changé avec succès"
        )
        return ResponseEntity.ok(message)
    }
}


data class RefreshRequest(
    val refreshToken: String
)