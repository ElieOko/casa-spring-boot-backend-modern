package server.web.casa.app.actor.application.service

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.TypeCard
import server.web.casa.app.actor.infrastructure.persistence.entity.TypeCardEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.TypeCardMapper
import server.web.casa.app.actor.infrastructure.persistence.repository.TypeCardRepository
import server.web.casa.app.address.domain.model.Country
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class TypeCardService(
   private val repository: TypeCardRepository,
   private val mapper: TypeCardMapper
) {
    suspend fun saveCard(data: TypeCard): TypeCard? {
        val data = mapper.toEntity(data)
        var result : TypeCardEntity? = null
        if (data!= null){
            result = repository.save(data)
        }
        return mapper.toDomain(result)
    }

    suspend fun findByIdTypeCard(id : Long) : TypeCard? {
        return repository.findById(id)?.let{ mapper.toDomain(it) }
    }
}