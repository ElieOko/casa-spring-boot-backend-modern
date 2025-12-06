package server.web.casa.app.actor.infrastructure.persistence.mapper.other

import org.springframework.stereotype.Component
import server.web.casa.app.actor.domain.model.Carreleur
import server.web.casa.app.actor.infrastructure.persistence.entity.other.CarreleurEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.TypeCardMapper
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper

@Component
class CarreleurMapper(
    private val userMapper: UserMapper,
    private val typeCardMapper: TypeCardMapper
) {
    fun toDomain(e: CarreleurEntity) : Carreleur {
            return Carreleur(
                carreleurId = e.carreleurId,
                firstName = e.firstName,
                lastName = e.lastName,
                fullName = e.fullName,
                address = e.address,
                images = e.images,
                cardFront = e.cardFront,
                cardBack = e.cardBack,
                user = userMapper.toDomain(e.user),
                typeCard = typeCardMapper.toDomain(e.typeCard),
                numberCard = e.numberCard
            )
    }

    fun toEntity(m: Carreleur): CarreleurEntity {
            return CarreleurEntity(
                carreleurId = m.carreleurId,
                firstName = m.firstName,
                lastName = m.lastName,
                fullName = m.fullName,
                address = m.address,
                images = m.images,
                cardFront = m.cardFront,
                cardBack = m.cardBack,
                user = userMapper.toEntityToDto(m.user),
                typeCard = typeCardMapper.toEntity(m.typeCard),
                numberCard = m.numberCard
            )
    }
}