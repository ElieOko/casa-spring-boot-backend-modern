package server.web.casa.app.actor.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.actor.application.service.*
import server.web.casa.app.actor.domain.model.*
import server.web.casa.app.address.application.service.CityService
import server.web.casa.app.user.application.*
import server.web.casa.app.user.domain.model.User
import server.web.casa.route.actor.ActorRoute
import server.web.casa.utils.Mode

const val ROUTE_ACTOR_BAILLEUR = ActorRoute.BAILLEUR

@Tag(name = "Bailleur", description = "Gestion des bailleurs")
@RestController
@RequestMapping(ROUTE_ACTOR_BAILLEUR)
@Profile(Mode.DEV)
class BailleurController(
   private val service : BailleurService,
   private val authService: AuthService,
   private val userService: UserService,
   private val cityService: CityService,
   private val typeAccountService: TypeAccountService,
   private val typeCardService: TypeCardService,
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        @Valid @RequestBody request: BailleurUser
    ): ResponseEntity<Map<String, Any?>> {
        val city = cityService.findByIdCity(request.user.cityId)
        val typeAccount = typeAccountService.findByIdTypeAccount(request.user.typeAccountId)
        val parrain : User? = null
        var paraintId : Long = 0
        if (request.user.typeAccountId != 3L){
            val response = mapOf("error" to "ce type n'est pas prise en charger pour compte bailleur")
            return ResponseEntity.badRequest().body(response)
        }
//        val typeCard = typeCardService.findByIdTypeCard(request.bailleur.typeCardId)
        if (city != null && typeAccount != null ) {
            if (request.bailleur.parrainId != null){
                paraintId = request.bailleur.parrainId
            }
//            parrain = userService.findIdUser(paraintId)
//            }
            val userSystem = User(
                userId = 0,
                password = request.user.password,
                typeAccount = typeAccount,
                email = request.user.email,
                phone = request.user.phone,
                city = city
            )
            val userCreated = authService.register(userSystem)
            val data = Bailleur(
                firstName = request.bailleur.firstName,
                lastName = request.bailleur.lastName,
                fullName = "${request.bailleur.firstName} ${request.bailleur.lastName}",
                address = request.bailleur.address,
                images = request.bailleur.images,
                cardFront = request.bailleur.cardFront,
                cardBack = request.bailleur.cardBack,
                parrain = null,
                user = userCreated.first,
                typeCard = null,
                numberCard = request.bailleur.numberCard,
                note = request.bailleur.note
            )
            val bailleutCreated = service.createBailleur(data)
            val response = mapOf(
                "message" to "Votre compte ${userCreated.first?.typeAccount?.name} a été créer avec succès",
                "user" to userCreated.first,
                "bailleur" to bailleutCreated,
                "token" to userCreated.second
            )
            return ResponseEntity.status(201).body(response)
        }
        val response = mapOf("error" to "Erreur au niveau de la validation des données")
        return ResponseEntity.badRequest().body(response)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllBailleur(): ResponseEntity<Map<String, List<Bailleur>>> {
        val data = service.findAllBailleur()
        val response = mapOf("bailleurs" to data)
        return ResponseEntity.ok().body(response)
    }



    @Operation(summary = "Modification bailleur")
    @PutMapping("/{id}")
    suspend fun updateBailleur(
        @PathVariable("id") id : Long,
        @RequestBody @Valid bailleur: Bailleur
    ) : ResponseEntity<Bailleur> {
        val updated = service.updateBailleur(id,bailleur)
        return ResponseEntity.ok(updated)
    }
}