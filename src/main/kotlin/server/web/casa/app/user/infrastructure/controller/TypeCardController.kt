package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.actor.infrastructure.persistence.entity.TypeCardEntity
import server.web.casa.app.actor.infrastructure.persistence.repository.TypeCardRepository
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
        ApiResponse(repository.findAll().toList())
    }
}