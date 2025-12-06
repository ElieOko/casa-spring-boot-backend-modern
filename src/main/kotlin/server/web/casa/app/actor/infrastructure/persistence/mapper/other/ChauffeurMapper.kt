package server.web.casa.app.actor.infrastructure.persistence.mapper.other

import org.springframework.stereotype.Component
import server.web.casa.app.actor.domain.model.Chauffeur
import server.web.casa.app.actor.infrastructure.persistence.entity.other.ChauffeurEntity
import server.web.casa.app.actor.infrastructure.persistence.mapper.TypeCardMapper
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper

@Component
class ChauffeurMapper(
    private val userMapper: UserMapper,
    private val typeCardMapper: TypeCardMapper
) {
    fun toDomain(e: ChauffeurEntity) : Chauffeur {
            return Chauffeur(
                chauffeurId = e.chauffeurId,
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

    fun toEntity(m: Chauffeur): ChauffeurEntity {
            return ChauffeurEntity(
                chauffeurId = m.chauffeurId,
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