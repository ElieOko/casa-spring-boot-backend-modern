package server.web.casa.app.actor.infrastructure.controller.master

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.actor.application.service.*
import server.web.casa.app.actor.domain.model.*
import server.web.casa.app.actor.domain.model.join.master.*
import server.web.casa.app.actor.domain.model.request.PersonRequest2
import server.web.casa.app.user.application.service.*
import server.web.casa.app.user.domain.model.*
import server.web.casa.route.actor.MemberScope
import server.web.casa.utils.*
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Personne", description = "Gestion des Membres")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)
class PersonController(
    private val service : PersonService,
    private val authService: AuthService,
    private val userService: UserService,
    private val accountService: AccountService,
    private val sentry: SentryService,
) {
//    private val log = LoggerFactory.getLogger(this::class.java)
    @PostMapping("/{version}/${MemberScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createPersonActor(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: PersonUserRequest
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val accountItems = request.account
            if (accountItems.isEmpty()) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Choisissez au moins un type de comptes.")
            val account = accountItems.map { accountService.findByIdAccount(it.typeAccount)}.first()
            val parrain : User? = null
            var paraintId : Long = 0
            //val phone =  normalizeAndValidatePhoneNumberUniversal(request.user.phone) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce numero n'est pas valide.")
            if (request.actor.parrainId != null) paraintId = request.actor.parrainId
            val userSystem = request.toUser(request.user.phone)
            val userCreated = authService.register(userSystem, accountItems)
            val data = request.toPerson(userCreated.first?.userId!!)
            val state = service.create(data)
            val response = mapOf(
                "message" to "Votre compte ${account.name} a été créer avec succès",
                "user" to userCreated.first,
                "actor" to state,
                "token" to userCreated.second
            )
            ResponseEntity.status(201).body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.person.createpersonactor.count",
                    distributionName = "api.person.createpersonactor.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${MemberScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPerson(request: HttpServletRequest): ApiResponse<List<Person>> = coroutineScope{
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            ApiResponse(service.findAllPerson().toList())
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.person.getallperson.count",
                    distributionName = "api.person.getallperson.latency"
                )
            )
        }
    }

    @Operation(summary = "Modification Person")
    @PutMapping("/{version}/${MemberScope.PROTECTED}/{id}")
    suspend fun updatePersonActor(
        httpRequest: HttpServletRequest,
        @PathVariable("id") id : Long,
        @RequestBody @Valid request: PersonRequest2) = coroutineScope {
            val startNanos = System.nanoTime()
        var statusCode = "200"
            try {
                val updated = service.update(request,id)
                userService.updateUsername(updated.second.userId!!,"@"+toPascalCase(updated.second.firstName + updated.second.lastName))
                val response = mapOf("message" to "Person updated successfully", "person" to updated.second, "user" to updated.first)
                ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
            } finally {
                sentry.callToMetric(
                    MetricModel(
                        startNanos = startNanos,
                        status = statusCode,
                        route = "${httpRequest.method} /${httpRequest.requestURI}",
                        countName = "api.person.updatepersonactor.count",
                        distributionName = "api.person.updatepersonactor.latency"
                    )
                )
            }
        }
    @Operation(summary = "Modification Photo de profile")
    @PutMapping("/{version}/${MemberScope.PROTECTED}/{id}/profile")
    suspend fun updatePersonImage(
        httpRequest: HttpServletRequest,
        @PathVariable("id") id : Long,
        @RequestBody @Valid request: ImageUserRequest
    ) : ResponseEntity<Map<String, Any>> = coroutineScope{
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val person = service.findByIdPerson(id)
            val updated = service.changeFile(request,person?.userId!!)
            val response = mapOf("person" to updated, "message" to "Image de profile modifier avec succès")
            ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.person.updatepersonimage.count",
                    distributionName = "api.person.updatepersonimage.latency"
                )
            )
        }
    }
}