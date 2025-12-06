package server.web.casa.app.actor.infrastructure.controller.other

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.actor.application.service.*
import server.web.casa.app.actor.application.service.other.PlombierService
import server.web.casa.app.actor.domain.model.*
import server.web.casa.app.actor.domain.model.join.other.PlombierUser
import server.web.casa.app.user.application.service.*
import server.web.casa.app.user.domain.model.*
import server.web.casa.route.actor.ActorRoute
import server.web.casa.utils.Mode
import server.web.casa.utils.normalizeAndValidatePhoneNumberUniversal
import server.web.casa.utils.toPascalCase

const val ROUTE_ACTOR_PLOMBIER = ActorRoute.PLOMBIER

@Tag(name = "PLOMBIER", description = "Gestion des PLOMBIER")
@RestController
@RequestMapping(ROUTE_ACTOR_PLOMBIER)
@Profile(Mode.DEV)
class PlombierController(
    private val service : PlombierService,
    private val authService: AuthService,
    private val typeAccountService: TypeAccountService,
    private val typeCardService: TypeCardService,
    private val userService: UserService
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createPlombier(
        @Valid @RequestBody request: PlombierUser
    ): ResponseEntity<Map<String, Any?>> {
        val typeAccount = typeAccountService.findByIdTypeAccount(request.user.typeAccountId)
        if (request.user.typeAccountId != 16L){
            val response = mapOf("error" to "ce type n'est pas prise en charger pour compte Plombier")
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
                username = "@"+toPascalCase("${request.plombier.firstName} ${request.plombier.lastName}")
            )
            val userCreated = authService.register(userSystem)
            val data = Plombier(
                firstName = request.plombier.firstName,
                lastName = request.plombier.lastName,
                fullName = "${request.plombier.firstName} ${request.plombier.lastName}",
                address = request.plombier.address,
                images = request.plombier.images,
                cardFront = request.plombier.cardFront,
                cardBack = request.plombier.cardBack,
                user = userCreated.first,
                typeCard = null,
                numberCard = request.plombier.numberCard
            )
            val created = service.create(data)
            val response = mapOf(
                "message" to "Votre compte ${userCreated.first?.typeAccount?.name} a été créer avec succès",
                "user" to userCreated.first,
                "plombier" to created,
                "token" to userCreated.second
            )
            return ResponseEntity.status(201).body(response)
        }
        val response = mapOf("error" to "Erreur au niveau de la validation des données")
        return ResponseEntity.badRequest().body(response)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPlombier(): ResponseEntity<Map<String, List<Plombier>>> {
        val data = service.findAll()
        val response = mapOf("plombiers" to data)
        return ResponseEntity.ok().body(response)
    }

    @Operation(summary = "Modification Plombier")
    @PutMapping("/{id}")
    suspend fun updatePlombier(
        @PathVariable("id") id : Long,
        @RequestBody @Valid data: Plombier
    ) : ResponseEntity<Plombier> {
        val updated = service.update(id,data)
        userService.updateUsername(data.user!!.userId,"@"+toPascalCase(data.firstName + data.lastName))
        return ResponseEntity.ok(updated)
    }
}