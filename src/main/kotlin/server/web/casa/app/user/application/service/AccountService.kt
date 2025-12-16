package server.web.casa.app.user.application.service

import kotlinx.coroutines.flow.*
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.user.domain.model.*
import server.web.casa.app.user.infrastructure.persistence.entity.toDomain
import server.web.casa.app.user.infrastructure.persistence.repository.AccountRepository
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class AccountService(
    private val repository: AccountRepository,
) {
    suspend fun save(data: Account): Account {
        val data = data.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun getAll(): Flow<Account> = repository.findAll().map { it.toDomain() }

    suspend fun findByIdAccount(id : Long): Account {
      val data = repository.findById(id)?: throw ResponseStatusException(HttpStatusCode.valueOf(404), "ID Is Not Found.")
        return data.toDomain()
    }
    suspend fun findAccountWithType(account : Long, type : Long): Account {
      val data = repository.findAll().filter { it.typeAccountId == account && it.typeAccountId == type }.toList()
      if (data.isEmpty()) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Ce compte et type ne sont pas prise en charge.")
      return data.first().toDomain()
    }
}