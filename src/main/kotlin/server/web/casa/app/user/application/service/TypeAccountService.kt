package server.web.casa.app.user.application.service

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.user.domain.model.TypeAccount
import server.web.casa.app.user.infrastructure.persistence.entity.TypeAccountEntity
import server.web.casa.app.user.infrastructure.persistence.mapper.*
import server.web.casa.app.user.infrastructure.persistence.repository.TypeAccountRepository
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class TypeAccountService(
  private val repository: TypeAccountRepository,
) {
    suspend fun saveAccount(data: TypeAccount): TypeAccount {
        val data = data.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun getAll() : List<TypeAccount> {
        val data= repository.findAll()
        return data.map {
            TypeAccountEntity(it!!.typeAccountId,it.name).toDomain()
        }.toList()
    }
    suspend fun findByIdTypeAccount(id : Long) : TypeAccount? {
      val data = repository.findById(id).orElse(null)
        return data.toDomain()
    }
    fun findByIdType(id : Long) : TypeAccount? {
        val data = repository.findById(id).orElse(null)
        return data.toDomain()
    }
}