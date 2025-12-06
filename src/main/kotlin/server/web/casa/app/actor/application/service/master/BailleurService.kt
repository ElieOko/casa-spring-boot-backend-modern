package server.web.casa.app.actor.application.service.master

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.Bailleur
import server.web.casa.app.actor.infrastructure.persistence.entity.master.BailleurEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.actor.infrastructure.persistence.mapper.master.BailleurMapper
import server.web.casa.app.actor.infrastructure.persistence.repository.BailleurRepository
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class BailleurService(
    private val repository: BailleurRepository,
    private val mapper: BailleurMapper,
    private val userMapper: UserMapper,
    private val cardMapper: TypeCardMapper
) {
    suspend fun createBailleur(bailleur: Bailleur): Bailleur {
        val data = mapper.toEntity(bailleur)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }

    suspend fun findAllBailleur() : List<Bailleur> {
        return repository.findAll().map { mapper.toDomain(it) }.toList()
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
                parrain = userMapper.toEntityToDto(bailleur.parrain),
                user = userMapper.toEntityToDto(bailleur.user),
                typeCard = cardMapper.toEntity( bailleur.typeCard),
                numberCard = bailleur.numberCard,
                note = bailleur.note
            )
            val updated = repository.save(entityToUpdate)
            return mapper.toDomain(updated)
        }
    }
}