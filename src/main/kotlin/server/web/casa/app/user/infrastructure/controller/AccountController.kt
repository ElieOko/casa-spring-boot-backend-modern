package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.flow.Flow
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import server.web.casa.app.user.application.service.AccountService
import server.web.casa.app.user.domain.model.Account
import server.web.casa.route.account.AccountRoute
import server.web.casa.utils.ApiResponse

@RestController
@RequestMapping(AccountRoute.ACCOUNT)
@Profile("dev")
class AccountController(
    private val service: AccountService,
) {
    @Operation(summary = "Liste de Accounts")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllAccountE(): ApiResponse<Flow<Account>> {
        val data = service.getAll()
        return ApiResponse(data)
    }
}