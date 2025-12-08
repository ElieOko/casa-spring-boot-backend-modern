package server.web.casa.app.actor.application.service.other

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.Electricien
import server.web.casa.app.actor.infrastructure.persistence.entity.other.ElectricienEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.actor.infrastructure.persistence.mapper.other.*
import server.web.casa.app.actor.infrastructure.persistence.repository.ElectricienRepository
import server.web.casa.app.user.infrastructure.persistence.mapper.toEntityToDto
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class ElectricienService(
    private val repository: ElectricienRepository,
) {
    suspend fun create(m: Electricien): Electricien {
        val data = m.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun findAll() : List<Electricien> {
        return repository.findAll().map { it.toDomain() }.toList()
    }
    suspend fun update(id : Long, l: Electricien): Electricien {
        repository.findById(id).let{
            val entityToUpdate = ElectricienEntity(
                electricienId = id,
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