package server.web.casa.app.actor.infrastructure.persistence.mapper

import org.springframework.stereotype.Component
import server.web.casa.app.actor.domain.model.Bailleur
import server.web.casa.app.actor.infrastructure.persistence.entity.BailleurEntity
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper

@Component
class BailleurMapper(
    private val userMapper: UserMapper,
    private val typeCardMapper: TypeCardMapper
) {
    fun toDomain(bailleurEntity: BailleurEntity) : Bailleur{
            return Bailleur(
                bailleurId = bailleurEntity.bailleurId,
                firstName = bailleurEntity.firstName,
                lastName = bailleurEntity.lastName,
                fullName = bailleurEntity.fullName,
                address = bailleurEntity.address,
                images = bailleurEntity.images,
                cardFront = bailleurEntity.cardFront,
                cardBack = bailleurEntity.cardBack,
                parrain = userMapper.toDomain(bailleurEntity.parrain),
                user = userMapper.toDomain(bailleurEntity.user),
                typeCard = typeCardMapper.toDomain(bailleurEntity.typeCard),
                numberCard = bailleurEntity.numberCard,
                note = bailleurEntity.note
            )
    }

    fun toEntity(bailleur: Bailleur): BailleurEntity {
            return BailleurEntity(
                bailleurId = bailleur.bailleurId,
                firstName = bailleur.firstName,
                lastName = bailleur.lastName,
                fullName = bailleur.fullName,
                address = bailleur.address,
                images = bailleur.images,
                cardFront = bailleur.cardFront,
                cardBack = bailleur.cardBack,
                parrain = userMapper.toEntityToDto(bailleur.parrain),
                user = userMapper.toEntityToDto(bailleur.user),
                typeCard = typeCardMapper.toEntity(bailleur.typeCard),
                numberCard = bailleur.numberCard,
                note = bailleur.note
            )
    }
}