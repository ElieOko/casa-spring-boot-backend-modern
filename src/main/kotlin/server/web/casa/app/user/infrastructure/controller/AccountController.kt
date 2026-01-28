package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import server.web.casa.app.user.application.service.AccountService
import server.web.casa.app.user.infrastructure.persistence.entity.AccountDTO
import server.web.casa.route.account.AccountScope

@RestController
@RequestMapping("api")
@Profile("dev")
class AccountController(
    private val service: AccountService,
) {
    @Operation(summary = "Liste de Accounts")
    @GetMapping("/{version}/${AccountScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllAccountE(): Map<String, List<AccountDTO>> = coroutineScope {
       mapOf("accounts" to service.getAll().toList())
    }
}