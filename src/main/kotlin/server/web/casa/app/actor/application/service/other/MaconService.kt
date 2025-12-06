package server.web.casa.app.actor.application.service.other

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.Carreleur
import server.web.casa.app.actor.domain.model.Chauffeur
import server.web.casa.app.actor.domain.model.Electricien
import server.web.casa.app.actor.domain.model.Frigoriste
import server.web.casa.app.actor.domain.model.Macon
import server.web.casa.app.actor.infrastructure.persistence.entity.other.CarreleurEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.other.ChauffeurEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.other.ElectricienEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.other.FrigoristeEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.other.MaconEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.actor.infrastructure.persistence.mapper.other.CarreleurMapper
import server.web.casa.app.actor.infrastructure.persistence.mapper.other.ChauffeurMapper
import server.web.casa.app.actor.infrastructure.persistence.mapper.other.ElectricienMapper
import server.web.casa.app.actor.infrastructure.persistence.mapper.other.FrigoristeMapper
import server.web.casa.app.actor.infrastructure.persistence.mapper.other.MaconMapper
import server.web.casa.app.actor.infrastructure.persistence.repository.CarreleurRepository
import server.web.casa.app.actor.infrastructure.persistence.repository.ChauffeurRepository
import server.web.casa.app.actor.infrastructure.persistence.repository.ElectricienRepository
import server.web.casa.app.actor.infrastructure.persistence.repository.FrigoristeRepository
import server.web.casa.app.actor.infrastructure.persistence.repository.MaconRepository
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class MaconService(
    private val repository: MaconRepository,
    private val mapper: MaconMapper,
    private val userMapper: UserMapper,
    private val cardMapper: TypeCardMapper
) {
    suspend fun create(m: Macon): Macon {
        val data = mapper.toEntity(m)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }
    suspend fun findAll() : List<Macon> {
        return repository.findAll().map { mapper.toDomain(it) }.toList()
    }
    suspend fun update(id : Long, l: Macon): Macon {
        repository.findById(id).let{
            val entityToUpdate = MaconEntity(
                maconId = id,
                firstName = l.firstName,
                lastName = l.lastName,
                fullName = l.fullName,
                address = l.address,
                images = l.images,
                cardFront = l.cardFront,
                cardBack = l.cardBack,
                user = userMapper.toEntityToDto(l.user),
                typeCard = cardMapper.toEntity(l.typeCard),
                numberCard = l.numberCard,
            )
            val updated = repository.save(entityToUpdate)
            return mapper.toDomain(updated)
        }
    }
}