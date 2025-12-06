package server.web.casa.app.actor.infrastructure.controller.other

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.actor.application.service.*
import server.web.casa.app.actor.application.service.other.MenusierService
import server.web.casa.app.actor.domain.model.*
import server.web.casa.app.actor.domain.model.join.other.MenusierUser
import server.web.casa.app.user.application.service.*
import server.web.casa.app.user.domain.model.*
import server.web.casa.route.actor.ActorRoute
import server.web.casa.utils.Mode
import server.web.casa.utils.normalizeAndValidatePhoneNumberUniversal
import server.web.casa.utils.toPascalCase

const val ROUTE_ACTOR_MENUSIER = ActorRoute.MENUSIER

@Tag(name = "MENUSIER", description = "Gestion des MENUSIER")
@RestController
@RequestMapping(ROUTE_ACTOR_MENUSIER)
@Profile(Mode.DEV)
class MenusierController(
    private val service : MenusierService,
    private val authService: AuthService,
    private val typeAccountService: TypeAccountService,
    private val typeCardService: TypeCardService,
    private val userService: UserService
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createMenusier(
        @Valid @RequestBody request: MenusierUser
    ): ResponseEntity<Map<String, Any?>> {
        val typeAccount = typeAccountService.findByIdTypeAccount(request.user.typeAccountId)
        if (request.user.typeAccountId != 11L){
            val response = mapOf("error" to "ce type n'est pas prise en charger pour compte Menusier")
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
                username = "@"+toPascalCase("${request.menusier.firstName} ${request.menusier.lastName}")
            )
            val userCreated = authService.register(userSystem)
            val data = Menusier(
                firstName = request.menusier.firstName,
                lastName = request.menusier.lastName,
                fullName = "${request.menusier.firstName} ${request.menusier.lastName}",
                address = request.menusier.address,
                images = request.menusier.images,
                cardFront = request.menusier.cardFront,
                cardBack = request.menusier.cardBack,
                user = userCreated.first,
                typeCard = null,
                numberCard = request.menusier.numberCard
            )
            val created = service.create(data)
            val response = mapOf(
                "message" to "Votre compte ${userCreated.first?.typeAccount?.name} a été créer avec succès",
                "user" to userCreated.first,
                "menusier" to created,
                "token" to userCreated.second
            )
            return ResponseEntity.status(201).body(response)
        }
        val response = mapOf("error" to "Erreur au niveau de la validation des données")
        return ResponseEntity.badRequest().body(response)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllMenusier(): ResponseEntity<Map<String, List<Menusier>>> {
        val data = service.findAll()
        val response = mapOf("menusiers" to data)
        return ResponseEntity.ok().body(response)
    }

    @Operation(summary = "Modification Menusier")
    @PutMapping("/{id}")
    suspend fun updateMenusier(
        @PathVariable("id") id : Long,
        @RequestBody @Valid data: Menusier
    ) : ResponseEntity<Menusier> {
        val updated = service.update(id,data)
        userService.updateUsername(data.user!!.userId,"@"+toPascalCase(data.firstName + data.lastName))
        return ResponseEntity.ok(updated)
    }
}