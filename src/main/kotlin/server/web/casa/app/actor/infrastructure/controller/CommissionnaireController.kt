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
import server.web.casa.app.user.domain.model.*
import server.web.casa.route.actor.ActorRoute
import server.web.casa.utils.Mode
import server.web.casa.utils.toPascalCase

const val ROUTE_ACTOR_COMMISSIONNAIRE = ActorRoute.COMMISSIONNAIRE

@Tag(name = "Commissionnaire", description = "Gestion des commissionnaires")
@RestController
@RequestMapping(ROUTE_ACTOR_COMMISSIONNAIRE)
@Profile(Mode.DEV)
class CommissionnaireController(
   private val service : CommissionnaireService,
   private val authService: AuthService,
   private val userService: UserService,
   private val cityService: CityService,
   private val typeAccountService: TypeAccountService,
   private val typeCardService: TypeCardService,
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        @Valid @RequestBody request: CommissionnaireUser
    ): ResponseEntity<Map<String, Any?>> {
//        val city = cityService.findByIdCity(request.user.cityId)
        val typeAccount = typeAccountService.findByIdTypeAccount(request.user.typeAccountId)
        if (request.user.typeAccountId != 2L){
            val response = mapOf("error" to "ce type n'est pas prise en charger pour compte commissionnaire")
            return ResponseEntity.badRequest().body(response)
        }
//        val typeCard = typeCardService.findByIdTypeCard(request.commissionnaire.typeCardId)
        if (typeAccount != null ) {
            val userSystem = User(
                password = request.user.password,
                typeAccount = typeAccount,
                email = request.user.email,
                phone = request.user.phone,
                city = request.user.city,
                country = request.user.country,
                username = "@"+toPascalCase("${request.commissionnaire.firstName} ${request.commissionnaire.lastName}")
            )
            val userCreated = authService.register(userSystem)
            val data = Commissionnaire(
                firstName = request.commissionnaire.firstName,
                lastName = request.commissionnaire.lastName,
                fullName = "${request.commissionnaire.firstName} ${request.commissionnaire.lastName}",
                address = request.commissionnaire.address,
                images = request.commissionnaire.images,
                cardFront = request.commissionnaire.cardFront,
                cardBack = request.commissionnaire.cardBack,
                user = userCreated.first,
                typeCard = null,
                numberCard = request.commissionnaire.numberCard
            )
            val bailleutCreated = service.createCommissionnaire(data)
            val response = mapOf(
                "message" to "Votre compte ${userCreated.first?.typeAccount?.name} a été créer avec succès",
                "user" to userCreated.first,
                "commissionnaire" to bailleutCreated,
                "token" to userCreated.second
            )
            return ResponseEntity.status(201).body(response)
        }
        val response = mapOf("error" to "Erreur au niveau de la validation des données")
        return ResponseEntity.badRequest().body(response)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllCommissionnaire(): ResponseEntity<Map<String, List<Commissionnaire>>> {
        val data = service.findAllCommissionnaire()
        val response = mapOf("commissionnaires" to data)
        return ResponseEntity.ok().body(response)
    }

    @Operation(summary = "Modification Commissionnaire")
    @PutMapping("/{id}")
    suspend fun updateCommissionnaire(
        @PathVariable("id") id : Long,
        @RequestBody @Valid commissionnaire: Commissionnaire
    ) : ResponseEntity<Commissionnaire> {
        val updated = service.updateCommissionnaire(id,commissionnaire)
        userService.updateUsername(commissionnaire.user!!.userId,"@"+toPascalCase(commissionnaire.firstName + commissionnaire.lastName))
        return ResponseEntity.ok(updated)
    }
}