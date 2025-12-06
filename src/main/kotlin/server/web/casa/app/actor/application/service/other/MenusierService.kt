package server.web.casa.app.actor.application.service.other

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.Menusier
import server.web.casa.app.actor.infrastructure.persistence.entity.other.MenusierEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.*
import server.web.casa.app.actor.infrastructure.persistence.mapper.other.MenusierMapper
import server.web.casa.app.actor.infrastructure.persistence.repository.MenusierRepository
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class MenusierService(
    private val repository: MenusierRepository,
    private val mapper: MenusierMapper,
    private val userMapper: UserMapper,
    private val cardMapper: TypeCardMapper
) {
    suspend fun create(m: Menusier): Menusier {
        val data = mapper.toEntity(m)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }
    suspend fun findAll() : List<Menusier> {
        return repository.findAll().map { mapper.toDomain(it) }.toList()
    }
    suspend fun update(id : Long, l: Menusier): Menusier {
        repository.findById(id).let{
            val entityToUpdate = MenusierEntity(
                menusierId = id,
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