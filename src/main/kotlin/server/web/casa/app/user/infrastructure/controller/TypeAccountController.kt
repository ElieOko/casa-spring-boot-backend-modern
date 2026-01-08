package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import server.web.casa.app.user.application.service.TypeAccountService
import server.web.casa.app.user.domain.model.TypeAccount
import server.web.casa.route.account.AccountRoute
import server.web.casa.utils.ApiResponse

@RestController
@RequestMapping(AccountRoute.ACCOUNT_TYPE)
@Profile("dev")
class TypeAccountController(
    private val service: TypeAccountService,
) {
    @Operation(summary = "Liste de Type Accounts")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllTypeAccountE(): ApiResponse<List<TypeAccount>> {
        val data = service.getAll().toList()
        return ApiResponse(data)
    }
}