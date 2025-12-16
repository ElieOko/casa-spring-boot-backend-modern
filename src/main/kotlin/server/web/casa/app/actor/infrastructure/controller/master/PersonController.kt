package server.web.casa.app.actor.infrastructure.controller.master

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.actor.application.service.*
import server.web.casa.app.actor.domain.model.*
import server.web.casa.app.actor.domain.model.join.master.PersonUserRequest
import server.web.casa.app.actor.domain.model.join.master.PersonUserUpdateRequest
import server.web.casa.app.actor.domain.model.request.PersonRequest
import server.web.casa.app.user.application.service.*
import server.web.casa.app.user.domain.model.User
import server.web.casa.route.actor.ActorRoute
import server.web.casa.utils.*

const val ROUTE_ACTOR_BAILLEUR = ActorRoute.BAILLEUR

@Tag(name = "Personne", description = "Gestion des acteurs")
@RestController
@RequestMapping(ROUTE_ACTOR_BAILLEUR)
@Profile(Mode.DEV)
class PersonController(
    private val service : PersonService,
    private val authService: AuthService,
    private val userService: UserService,
    private val typeAccountService: TypeAccountService,
    private val accountService: AccountService,
//    private val typeCardService: TypeCardService,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createPerson(
        @Valid @RequestBody request: PersonUserRequest
    ): ResponseEntity<Map<String, Any?>> {
        val account = accountService.findAccountWithType(request.account.account, request.account.typeAccount)
        val parrain : User? = null
        var paraintId : Long = 0
        val phone =  normalizeAndValidatePhoneNumberUniversal(request.user.phone) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce numero n'est pas valide.")
        if (request.actor.parrainId != null){
            paraintId = request.actor.parrainId
        }
        val userSystem = User(
            userId = 0,
            password = request.user.password,
            email = request.user.email,
            phone = phone,
            username = "@"+toPascalCase("${request.actor.firstName} ${request.actor.lastName}"),
            city = request.user.city,
            country =  request.user.country
        )
//            toPascalCase("${request.actor.firstName} ${request.actor.lastName}")
        val userCreated = authService.register(userSystem)
        val data = Person(
            firstName = request.actor.firstName,
            lastName = request.actor.lastName,
            fullName = "${request.actor.firstName} ${request.actor.lastName}",
            address = request.actor.address,
            images = request.actor.images,
            cardFront = request.actor.cardFront,
            cardBack = request.actor.cardBack,
            parrainId = null ,
            userId = userCreated.first?.userId,
            typeCardId = null,
            numberCard = request.actor.numberCard,
            )
        val state = service.create(data)
        val response = mapOf(
            "message" to "Votre compte ${account.name} a été créer avec succès",
            "user" to userCreated.first,
            "actor" to state,
            "token" to userCreated.second
        )
        return ResponseEntity.status(201).body(response)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPerson(): ApiResponse<Flow<Person>> {
        return ApiResponse(service.findAllPerson())
    }

//    @Operation(summary = "Modification Person")
//    @PutMapping("/{id}")
//    suspend fun updatePerson(
//        @PathVariable("id") id : Long,
//        @RequestBody @Valid request: PersonUserUpdateRequest
//    ) : ResponseEntity<Person> {
//        val updated = service.update(id,request.user)
//        userService.updateUsername(request.user.userId,"@"+toPascalCase(bailleur.firstName + bailleur.lastName))
//        return ResponseEntity.ok(updated)
//    }
}