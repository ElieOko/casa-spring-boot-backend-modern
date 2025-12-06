package server.web.casa.app.actor.infrastructure.controller.other

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.actor.application.service.*
import server.web.casa.app.actor.application.service.other.MaconService
import server.web.casa.app.actor.domain.model.*
import server.web.casa.app.actor.domain.model.join.other.MaconUser
import server.web.casa.app.user.application.service.*
import server.web.casa.app.user.domain.model.*
import server.web.casa.route.actor.ActorRoute
import server.web.casa.utils.Mode
import server.web.casa.utils.normalizeAndValidatePhoneNumberUniversal
import server.web.casa.utils.toPascalCase

const val ROUTE_ACTOR_MACON = ActorRoute.MACON

@Tag(name = "Maçon", description = "Gestion des Maçon")
@RestController
@RequestMapping(ROUTE_ACTOR_MACON)
@Profile(Mode.DEV)
class MaconController(
    private val service : MaconService,
    private val authService: AuthService,
    private val typeAccountService: TypeAccountService,
    private val typeCardService: TypeCardService,
    private val userService: UserService
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createMacon(
        @Valid @RequestBody request: MaconUser
    ): ResponseEntity<Map<String, Any?>> {
        val typeAccount = typeAccountService.findByIdTypeAccount(request.user.typeAccountId)
        if (request.user.typeAccountId != 18L){
            val response = mapOf("error" to "ce type n'est pas prise en charger pour compte Maçon")
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
                username = "@"+toPascalCase("${request.macon.firstName} ${request.macon.lastName}")
            )
            val userCreated = authService.register(userSystem)
            val data = Macon(
                firstName = request.macon.firstName,
                lastName = request.macon.lastName,
                fullName = "${request.macon.firstName} ${request.macon.lastName}",
                address = request.macon.address,
                images = request.macon.images,
                cardFront = request.macon.cardFront,
                cardBack = request.macon.cardBack,
                user = userCreated.first,
                typeCard = null,
                numberCard = request.macon.numberCard
            )
            val created = service.create(data)
            val response = mapOf(
                "message" to "Votre compte ${userCreated.first?.typeAccount?.name} a été créer avec succès",
                "user" to userCreated.first,
                "macon" to created,
                "token" to userCreated.second
            )
            return ResponseEntity.status(201).body(response)
        }
        val response = mapOf("error" to "Erreur au niveau de la validation des données")
        return ResponseEntity.badRequest().body(response)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllMacon(): ResponseEntity<Map<String, List<Macon>>> {
        val data = service.findAll()
        val response = mapOf("macons" to data)
        return ResponseEntity.ok().body(response)
    }

    @Operation(summary = "Modification Maçon")
    @PutMapping("/{id}")
    suspend fun updateMacon(
        @PathVariable("id") id : Long,
        @RequestBody @Valid data: Macon
    ) : ResponseEntity<Macon> {
        val updated = service.update(id,data)
        userService.updateUsername(data.user!!.userId,"@"+toPascalCase(data.firstName + data.lastName))
        return ResponseEntity.ok(updated)
    }
}