package server.web.casa.app.actor.application.service.other

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.Ajusteur
import server.web.casa.app.actor.infrastructure.persistence.entity.other.AjusteurEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.actor.infrastructure.persistence.mapper.other.*
import server.web.casa.app.actor.infrastructure.persistence.repository.AjusteurRepository
import server.web.casa.app.user.infrastructure.persistence.mapper.toEntityToDto
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class AjusteurService(private val repository: AjusteurRepository) {
    suspend fun create(m: Ajusteur): Ajusteur {
        val data = m.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun findAll() : List<Ajusteur>  = repository.findAll().map { it.toDomain() }.toList()

    suspend fun update(id : Long, l: Ajusteur): Ajusteur {
        repository.findById(id).let{
            val entityToUpdate = AjusteurEntity(
                ajusteurId = id,
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