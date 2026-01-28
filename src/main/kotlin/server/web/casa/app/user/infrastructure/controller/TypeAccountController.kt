package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import server.web.casa.app.user.application.service.TypeAccountService
import server.web.casa.app.user.domain.model.TypeAccount
import server.web.casa.route.account.AccountTypeScope
import server.web.casa.utils.ApiResponse

@RestController
@RequestMapping()
@Profile("dev")
class TypeAccountController(
    private val service: TypeAccountService,
) {
    @Operation(summary = "Liste de Type Accounts")
    @GetMapping("/{version}/${AccountTypeScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllTypeAccountE(): ApiResponse<List<TypeAccount>> = coroutineScope {
       ApiResponse(service.getAll().toList())
    }
}