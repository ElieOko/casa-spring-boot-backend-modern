package server.web.casa.app.actor.infrastructure.persistence.mapper

import org.springframework.stereotype.Component
import server.web.casa.app.actor.domain.model.Locataire
import server.web.casa.app.actor.infrastructure.persistence.entity.master.LocataireEntity
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper

@Component
class LocataireMapper(
    private val userMapper: UserMapper,
    private val typeCardMapper: TypeCardMapper
) {
    fun toDomain(locataireEntity: LocataireEntity) : Locataire{
            return Locataire(
                locataireId = locataireEntity.locataireId,
                firstName = locataireEntity.firstName,
                lastName = locataireEntity.lastName,
                fullName = locataireEntity.fullName,
                address = locataireEntity.address,
                images = locataireEntity.images,
                cardFront = locataireEntity.cardFront,
                cardBack = locataireEntity.cardBack,
                user = userMapper.toDomain(locataireEntity.user),
                typeCard = typeCardMapper.toDomain(locataireEntity.typeCard),
                numberCard = locataireEntity.numberCard
            )
    }

    fun toEntity(locataire: Locataire): LocataireEntity {
            return LocataireEntity(
                locataireId = locataire.locataireId,
                firstName = locataire.firstName,
                lastName = locataire.lastName,
                fullName = locataire.fullName,
                address = locataire.address,
                images = locataire.images,
                cardFront = locataire.cardFront,
                cardBack = locataire.cardBack,
                user = userMapper.toEntityToDto(locataire.user),
                typeCard = typeCardMapper.toEntity(locataire.typeCard),
                numberCard = locataire.numberCard
            )
    }
}