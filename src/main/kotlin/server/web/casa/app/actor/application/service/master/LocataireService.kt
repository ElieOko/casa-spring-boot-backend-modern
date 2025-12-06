package server.web.casa.app.actor.application.service.master

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.Locataire
import server.web.casa.app.actor.infrastructure.persistence.entity.master.LocataireEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.actor.infrastructure.persistence.repository.LocataireRepository
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class LocataireService(
    private val repository: LocataireRepository,
    private val mapper: LocataireMapper,
    private val userMapper: UserMapper,
    private val cardMapper: TypeCardMapper
) {
    suspend fun createLocataire(locataire: Locataire): Locataire {
        val data = mapper.toEntity(locataire)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }

    suspend fun findAllLocataire() : List<Locataire> {
        return repository.findAll().map { mapper.toDomain(it) }.toList()
    }
    suspend fun updateLocataire(id : Long, l: Locataire): Locataire {
        repository.findById(id).let{
            val entityToUpdate = LocataireEntity(
                locataireId = id,
                firstName = l.firstName,
                lastName = l.lastName,
                fullName = l.fullName,
                address = l.address,
                images = l.images,
                cardFront = l.cardFront,
                cardBack = l.cardBack,
                user = userMapper.toEntityToDto(l.user),
                typeCard = cardMapper.toEntity( l.typeCard),
                numberCard = l.numberCard,
            )
            val updated = repository.save(entityToUpdate)
            return mapper.toDomain(updated)
        }
    }
}