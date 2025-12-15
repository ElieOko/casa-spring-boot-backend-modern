package server.web.casa.app.user.application.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.user.domain.model.TypeAccount
import server.web.casa.app.user.infrastructure.persistence.entity.TypeAccountEntity
import server.web.casa.app.user.infrastructure.persistence.mapper.*
import server.web.casa.app.user.infrastructure.persistence.repository.AccountRepository
import server.web.casa.app.user.infrastructure.persistence.repository.TypeAccountRepository
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class AccountService(
    private val repository: AccountRepository,
) {
    suspend fun saveAccount(data: Account): Account {
        val data = data.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun getAll(): Flow<TypeAccount> {
        val data= repository.findAll()
        return data.map {
            TypeAccountEntity(it.typeAccountId,it.name).toDomain()
        }
    }
    suspend fun findByIdTypeAccount(id : Long) : TypeAccount? {
      val data = repository.findById(id)
        return data?.toDomain()
    }
    suspend fun findByIdType(id : Long) : TypeAccount? {
        val data = repository.findById(id)
        return data?.toDomain()
    }
}