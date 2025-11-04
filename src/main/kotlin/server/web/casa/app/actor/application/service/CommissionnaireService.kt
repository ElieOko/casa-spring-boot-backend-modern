package server.web.casa.app.actor.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.Commissionnaire
import server.web.casa.app.actor.domain.model.Locataire
import server.web.casa.app.actor.infrastructure.persistence.entity.CommissionnaireEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.LocataireEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.CommissionnaireMapper
import server.web.casa.app.actor.infrastructure.persistence.mapper.TypeCardMapper
import server.web.casa.app.actor.infrastructure.persistence.repository.CommissionnaireRepository
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class CommissionnaireService(
    private val repository: CommissionnaireRepository,
    private val mapper: CommissionnaireMapper,
    private val userMapper: UserMapper,
    private val cardMapper: TypeCardMapper
) {
    suspend fun createCommissionnaire(commissionnaire: Commissionnaire): Commissionnaire {
        val data = mapper.toEntity(commissionnaire)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }

    suspend fun findAllCommissionnaire() : List<Commissionnaire> {
        return repository.findAll().map { mapper.toDomain(it) }.toList()
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
                user = userMapper.toEntity(c.user),
                typeCard = cardMapper.toEntity( c.typeCard),
                numberCard = c.numberCard,
            )
            val updated = repository.save(entityToUpdate)
            return mapper.toDomain(updated)
        }
    }
}