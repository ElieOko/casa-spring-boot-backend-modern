package server.web.casa.app.user.application

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.TypeCard
import server.web.casa.app.address.domain.model.City
import server.web.casa.app.user.domain.model.TypeAccount
import server.web.casa.app.user.infrastructure.persistence.entity.TypeAccountEntity
import server.web.casa.app.user.infrastructure.persistence.mapper.TypeAccountMapper
import server.web.casa.app.user.infrastructure.persistence.repository.TypeAccountRepository
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class TypeAccountService(
  private val repository: TypeAccountRepository,
  private val mapper: TypeAccountMapper
) {

    suspend fun saveAccount(data: TypeAccount): TypeAccount {
        val data = mapper.toEntity(data)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }

    suspend fun getAll() : List<TypeAccount> {
        val data= repository.findAll()
        return data.map {
            mapper.toDomain(TypeAccountEntity(it!!.typeAccountId,it.name))
        }.toList()
    }

    suspend fun findByIdTypeAccount(id : Long) : TypeAccount? {
      repository.findById(id)?.let {
            return mapper.toDomain(it)
        }
        return null
    }
}