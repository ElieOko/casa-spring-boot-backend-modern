package server.web.casa.app.actor.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.actor.application.service.*
import server.web.casa.app.actor.domain.model.*
import server.web.casa.app.address.application.service.CityService
import server.web.casa.app.user.application.*
import server.web.casa.app.user.domain.model.*
import server.web.casa.route.actor.ActorRoute
import server.web.casa.utils.Mode

const val ROUTE_ACTOR_LOCATAIRE = ActorRoute.LOCATAIRE

@Tag(name = "Locataire", description = "Gestion des locataires")
@RestController
@RequestMapping(ROUTE_ACTOR_LOCATAIRE)
@Profile(Mode.DEV)
class LocataireController(
   private val service : LocataireService,
   private val authService: AuthService,
   private val cityService: CityService,
   private val typeAccountService: TypeAccountService,
   private val typeCardService: TypeCardService,
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        @Valid @RequestBody request: LocataireUser
    ): ResponseEntity<Map<String, Any?>> {
        val city = cityService.findByIdCity(request.user.cityId)
        val typeAccount = typeAccountService.findByIdTypeAccount(request.user.typeAccountId)
        if (request.user.typeAccountId != 4L){
            val response = mapOf("error" to "ce type n'est pas prise en charger pour compte Locataire")
            return ResponseEntity.badRequest().body(response)
        }
//        val typeCard = typeCardService.findByIdTypeCard(request.locataire.typeCardId)
        if (city != null && typeAccount != null) {
            val userSystem = User(
                userId = 0,
                password = request.user.password,
                typeAccount = typeAccount,
                email = request.user.email,
                phone = request.user.phone,
                city = city
            )
            val userCreated = authService.register(userSystem)
            val data = Locataire(
                firstName = request.locataire.firstName,
                lastName = request.locataire.lastName,
                fullName = "${request.locataire.firstName} ${request.locataire.lastName}",
                address = request.locataire.address,
                images = request.locataire.images,
                cardFront = request.locataire.cardFront,
                cardBack = request.locataire.cardBack,
                user = userCreated.first,
                typeCard = null,
                numberCard = request.locataire.numberCard
            )
            val bailleutCreated = service.createLocataire(data)
            val response = mapOf(
                "message" to "Votre compte ${userCreated.first?.typeAccount?.name} a été créer avec succès",
                "user" to userCreated.first,
                "locataire" to bailleutCreated,
                "token" to userCreated.second
            )
            return ResponseEntity.status(201).body(response)
        }
        val response = mapOf("error" to "Erreur au niveau de la validation des données")
        return ResponseEntity.badRequest().body(response)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllLocataire(): ResponseEntity<Map<String, List<Locataire>>> {
        val data = service.findAllLocataire()
        val response = mapOf("locataires" to data)
        return ResponseEntity.ok().body(response)
    }
}