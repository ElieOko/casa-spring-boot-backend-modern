package server.web.casa.app.actor.infrastructure.controller.other

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.actor.application.service.*
import server.web.casa.app.actor.application.service.other.FrigoristeService
import server.web.casa.app.actor.domain.model.*
import server.web.casa.app.actor.domain.model.join.other.ElectricienUser
import server.web.casa.app.actor.domain.model.join.other.FrigoristeUser
import server.web.casa.app.user.application.service.*
import server.web.casa.app.user.domain.model.*
import server.web.casa.route.actor.ActorRoute
import server.web.casa.utils.Mode
import server.web.casa.utils.normalizeAndValidatePhoneNumberUniversal
import server.web.casa.utils.toPascalCase

const val ROUTE_ACTOR_FRIGORISTE = ActorRoute.FRIGORISTE

@Tag(name = "FRIGORISTE", description = "Gestion des FRIGORISTE")
@RestController
@RequestMapping(ROUTE_ACTOR_FRIGORISTE)
@Profile(Mode.DEV)
class FrigoristeController(
    private val service : FrigoristeService,
    private val authService: AuthService,
    private val typeAccountService: TypeAccountService,
    private val typeCardService: TypeCardService,
    private val userService: UserService
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFrigoriste(
        @Valid @RequestBody request: FrigoristeUser
    ): ResponseEntity<Map<String, Any?>> {
        val typeAccount = typeAccountService.findByIdTypeAccount(request.user.typeAccountId)
        if (request.user.typeAccountId != 12L){
            val response = mapOf("error" to "ce type n'est pas prise en charger pour compte le service Frigoriste")
            return ResponseEntity.badRequest().body(response)
        }
        val phone =  normalizeAndValidatePhoneNumberUniversal(request.user.phone) ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Ce numero n'est pas valide.")
//        val typeCard = typeCardService.findByIdTypeCard(request.locataire.typeCardId)
        if (typeAccount != null) {
            val userSystem = User(
                userId = 0,
                password = request.user.password,
                typeAccount = typeAccount,
                email = request.user.email,
                phone = phone,
                city = request.user.city,
                country = request.user.country,
                username = "@"+toPascalCase("${request.frigoriste.firstName} ${request.frigoriste.lastName}")
            )
            val userCreated = authService.register(userSystem)
            val data = Frigoriste(
                firstName = request.frigoriste.firstName,
                lastName = request.frigoriste.lastName,
                fullName = "${request.frigoriste.firstName} ${request.frigoriste.lastName}",
                address = request.frigoriste.address,
                images = request.frigoriste.images,
                cardFront = request.frigoriste.cardFront,
                cardBack = request.frigoriste.cardBack,
                user = userCreated.first,
                typeCard = null,
                numberCard = request.frigoriste.numberCard
            )
            val created = service.create(data)
            val response = mapOf(
                "message" to "Votre compte ${userCreated.first?.typeAccount?.name} a été créer avec succès",
                "user" to userCreated.first,
                "frigoriste" to created,
                "token" to userCreated.second
            )
            return ResponseEntity.status(201).body(response)
        }
        val response = mapOf("error" to "Erreur au niveau de la validation des données")
        return ResponseEntity.badRequest().body(response)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFrigoriste(): ResponseEntity<Map<String, List<Frigoriste>>> {
        val data = service.findAll()
        val response = mapOf("frigoristes" to data)
        return ResponseEntity.ok().body(response)
    }

    @Operation(summary = "Modification Frigoriste")
    @PutMapping("/{id}")
    suspend fun updateFrigoriste(
        @PathVariable("id") id : Long,
        @RequestBody @Valid data: Frigoriste
    ) : ResponseEntity<Frigoriste> {
        val updated = service.update(id,data)
        userService.updateUsername(data.user!!.userId,"@"+toPascalCase(data.firstName + data.lastName))
        return ResponseEntity.ok(updated)
    }
}