package server.web.casa.app.actor.application.service.master

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.Commissionnaire
import server.web.casa.app.actor.infrastructure.persistence.entity.master.CommissionnaireEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.master.*
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.actor.infrastructure.persistence.repository.CommissionnaireRepository
import server.web.casa.app.user.infrastructure.persistence.mapper.*
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class CommissionnaireService(private val repository: CommissionnaireRepository) {
    suspend fun createCommissionnaire(commissionnaire: Commissionnaire): Commissionnaire {
        val data = commissionnaire.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }

    suspend fun findAllCommissionnaire() : List<Commissionnaire> {
        return repository.findAll().map { it.toDomain() }.toList()
    }

    suspend fun updateCommissionnaire(id : Long, c: Commissionnaire): Commissionnaire {
        repository.findById(id).let{
            val entityToUpdate = CommissionnaireEntity(
                commissionnaireId = id,
                firstName = c.firstName,
                lastName = c.lastName,
                fullName = c.fullName,
                address = c.address,
                images = c.images,
                cardFront =c.cardFront,
                cardBack = c.cardBack,
                user = c.user?.toEntityToDto(),
                typeCard = c.typeCard?.toEntity(),
                numberCard = c.numberCard,
            )
            val updated = repository.save(entityToUpdate)
            return updated.toDomain()
        }
    }
}