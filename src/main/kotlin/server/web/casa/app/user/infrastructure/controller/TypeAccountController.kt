package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.user.application.TypeAccountService
import server.web.casa.app.user.domain.model.TypeAccount

@Tag(name = "Type de Compte", description = "Gestion des comptes")
@RestController
@RequestMapping("api/account")
class TypeAccountController(
    private val service: TypeAccountService
) {
    @Operation(summary = "Liste des comptes")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getListTypeAccount(): ResponseEntity<Map<String, List<TypeAccount>>> {
        val data = service.getAll()
        val mapResponse = mapOf("type_accounts" to data)
        return ResponseEntity.ok().body(mapResponse)
    }
}