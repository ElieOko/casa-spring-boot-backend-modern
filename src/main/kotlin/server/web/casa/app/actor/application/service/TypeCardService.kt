package server.web.casa.app.actor.application.service

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.TypeCard
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.actor.infrastructure.persistence.repository.TypeCardRepository
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class TypeCardService(private val repository: TypeCardRepository) {
    suspend fun saveCard(data: TypeCard): TypeCard? {
        val data = data.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }

    suspend fun findByIdTypeCard(id : Long) : TypeCard?  = repository.findById(id).orElse(null).toDomain()

}