package server.web.casa.app.actor.application.service.second

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.Demenagement
import server.web.casa.app.actor.infrastructure.persistence.entity.second.DemenagementEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.actor.infrastructure.persistence.mapper.second.DemenagementMapper
import server.web.casa.app.actor.infrastructure.persistence.repository.DemenagementRepository
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class DemenagementService(
    private val repository: DemenagementRepository,
    private val mapper: DemenagementMapper,
    private val userMapper: UserMapper,
    private val cardMapper: TypeCardMapper
) {
    suspend fun create(m: Demenagement): Demenagement {
        val data = mapper.toEntity(m)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }
    suspend fun findAll() : List<Demenagement> {
        return repository.findAll().map { mapper.toDomain(it) }.toList()
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
                user = userMapper.toEntityToDto(l.user),
                typeCard = cardMapper.toEntity(l.typeCard),
                numberCard = l.numberCard,
            )
            val updated = repository.save(entityToUpdate)
            return mapper.toDomain(updated)
        }
    }
}