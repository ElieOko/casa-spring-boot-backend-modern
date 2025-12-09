package server.web.casa.app.actor.application.service.master

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.Bailleur
import server.web.casa.app.actor.infrastructure.persistence.entity.master.BailleurEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.actor.infrastructure.persistence.mapper.master.*
import server.web.casa.app.actor.infrastructure.persistence.repository.BailleurRepository
import server.web.casa.app.user.infrastructure.persistence.mapper.toEntityToDto
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class BailleurService(private val repository: BailleurRepository) {
    suspend fun createBailleur(bailleur: Bailleur): Bailleur {
        val data = bailleur.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun findAllBailleur() : List<Bailleur> {
        return repository.findAll().map { it.toDomain() }.toList()
    }
    suspend fun updateBailleur(id : Long, bailleur: Bailleur): Bailleur {
        repository.findById(id).let{
            val entityToUpdate = BailleurEntity(
                bailleurId = id,
                firstName = bailleur.firstName,
                lastName = bailleur.lastName,
                fullName = bailleur.firstName + bailleur.lastName,
                address = bailleur.address,
                images = bailleur.images,
                cardFront = bailleur.cardFront,
                cardBack = bailleur.cardBack,
                parrain = bailleur.parrain?.toEntityToDto(),
                user = bailleur.user?.toEntityToDto(),
                typeCard = bailleur.typeCard?.toEntity(),
                numberCard = bailleur.numberCard,
                note = bailleur.note
            )
            val updated = repository.save(entityToUpdate)
            return updated.toDomain()
        }
    }
}