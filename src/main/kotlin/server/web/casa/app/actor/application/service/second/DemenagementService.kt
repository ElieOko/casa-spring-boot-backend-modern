package server.web.casa.app.actor.application.service.second

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.Demenagement
import server.web.casa.app.actor.infrastructure.persistence.entity.second.DemenagementEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.second.*
import server.web.casa.app.actor.infrastructure.persistence.mapper.toEntity
import server.web.casa.app.actor.infrastructure.persistence.repository.DemenagementRepository
import server.web.casa.app.user.infrastructure.persistence.mapper.toEntityToDto
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class DemenagementService(private val repository: DemenagementRepository) {
    suspend fun create(m: Demenagement): Demenagement {
        val data = m.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun findAll() : List<Demenagement> {
        return repository.findAll().map { it.toDomain() }.toList()
    }
    suspend fun update(id : Long, l: Demenagement): Demenagement {
        repository.findById(id).let{
            val entityToUpdate = DemenagementEntity(
                demenagementId = id,
                firstName = l.firstName,
                lastName = l.lastName,
                fullName = l.fullName,
                address = l.address,
                images = l.images,
                cardFront = l.cardFront,
                cardBack = l.cardBack,
                user = l.user?.toEntityToDto(),
                typeCard = l.typeCard?.toEntity(),
                numberCard = l.numberCard,
            )
            val updated = repository.save(entityToUpdate)
            return updated.toDomain()
        }
    }
}