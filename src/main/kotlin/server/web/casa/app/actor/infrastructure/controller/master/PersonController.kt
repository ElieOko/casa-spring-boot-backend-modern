package server.web.casa.app.actor.infrastructure.controller.master

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.actor.application.service.*
import server.web.casa.app.actor.domain.model.*
import server.web.casa.app.actor.domain.model.join.master.PersonUserRequest
import server.web.casa.app.actor.domain.model.join.master.toPerson
import server.web.casa.app.actor.domain.model.join.master.toUser
import server.web.casa.app.actor.domain.model.request.PersonRequest
import server.web.casa.app.user.application.service.*
import server.web.casa.app.user.domain.model.ImageUserRequest
import server.web.casa.app.user.domain.model.User
import server.web.casa.route.actor.ActorRoute.MEMBER
import server.web.casa.utils.*
import kotlin.collections.isNotEmpty

@Tag(name = "Personne", description = "Gestion des Membres")
@RestController
@RequestMapping(MEMBER)
@Profile(Mode.DEV)
class PersonController(
    private val service : PersonService,
    private val authService: AuthService,
    private val userService: UserService,
    private val typeAccountService: TypeAccountService,
    private val accountService: AccountService
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createPerson(
        @Valid @RequestBody request: PersonUserRequest
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val accountItems = request.account
        if (accountItems.isNotEmpty()){
            val account = accountItems.map {
                typeAccountService.findByIdTypeAccount(it.typeAccount)
            }.first()
            val parrain : User? = null
            var paraintId : Long = 0
            val phone =  normalizeAndValidatePhoneNumberUniversal(request.user.phone) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce numero n'est pas valide.")
            if (request.actor.parrainId != null){
                paraintId = request.actor.parrainId
            }
            val userSystem = request.toUser(phone)
            val userCreated = authService.register(userSystem, accountItems)
            val data = request.toPerson(userCreated.first?.userId!!)
            val state = service.create(data)
            val response = mapOf(
                "message" to "Votre compte ${account.name} a été créer avec succès",
                "user" to userCreated.first,
                "actor" to state,
                "token" to userCreated.second
            )
            ResponseEntity.status(201).body(response)
        }
        throw ResponseStatusException(HttpStatus.CONFLICT)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPerson(): ApiResponse<List<Person>> {
        return ApiResponse(service.findAllPerson().toList())
    }

    @Operation(summary = "Modification Person")
    @PutMapping("/{id}")
    suspend fun updatePerson(
        @PathVariable("id") id : Long,
        @RequestBody @Valid request: PersonRequest
    ) : ResponseEntity<Person> = coroutineScope {
        val updated = service.update(request,id)
        userService.updateUsername(updated.userId!!,"@"+toPascalCase(updated.firstName + updated.lastName))
        ResponseEntity.ok(updated)
    }
    @Operation(summary = "Modification Photo de profile")
    @PutMapping("/{id}/profile")
    suspend fun updatePersonImage(
        @PathVariable("id") id : Long,
        @RequestBody @Valid request: ImageUserRequest
    ) : ResponseEntity<Map<String, Any>> = coroutineScope{
        val person = service.findByIdPerson(id)
        val updated = service.changeFile(request,person?.userId!!)
        val response = mapOf("person" to updated, "message" to "Image de profile modifier avec succès")
        ResponseEntity.ok(response)
    }
}