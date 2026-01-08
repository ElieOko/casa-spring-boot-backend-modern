package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import server.web.casa.app.actor.infrastructure.persistence.entity.TypeCardEntity
import server.web.casa.app.actor.infrastructure.persistence.repository.TypeCardRepository
import server.web.casa.app.user.application.service.TypeAccountService
import server.web.casa.app.user.domain.model.TypeAccount
import server.web.casa.route.account.AccountRoute
import server.web.casa.route.utils.AgenceRoute.CARD_TYPE
import server.web.casa.utils.ApiResponse

@RestController
@RequestMapping(CARD_TYPE)
@Profile("dev")
class TypeCardController(
    private val repository: TypeCardRepository,
) {
    @Operation(summary = "Liste de Type card")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllTypeCard(): ApiResponse<List<TypeCardEntity>> = coroutineScope {
        val data = repository.findAll().toList()
        ApiResponse(data)
    }
}