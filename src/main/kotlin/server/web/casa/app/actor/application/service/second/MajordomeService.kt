package server.web.casa.app.actor.application.service.second

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.Majordome
import server.web.casa.app.actor.infrastructure.persistence.entity.second.MajordomeEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.actor.infrastructure.persistence.mapper.second.*
import server.web.casa.app.actor.infrastructure.persistence.repository.MajordomeRepository
import server.web.casa.app.user.infrastructure.persistence.mapper.toEntityToDto
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class MajordomeService(private val repository: MajordomeRepository) {
    suspend fun create(m: Majordome): Majordome {
        val data = m.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun findAll() : List<Majordome> = repository.findAll().map { it.toDomain() }.toList()

    suspend fun update(id : Long, l: Majordome): Majordome {
        repository.findById(id).let{
            val entityToUpdate = MajordomeEntity(
                majordomeId = id,
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