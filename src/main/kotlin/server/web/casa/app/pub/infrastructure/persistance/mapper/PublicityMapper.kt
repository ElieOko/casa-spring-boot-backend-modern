package server.web.casa.app.pub.infrastructure.persistance.mapper

import org.springframework.stereotype.Component
import server.web.casa.app.pub.domain.model.Publicity
import server.web.casa.app.pub.infrastructure.persistance.entity.PublicityEntity
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper

@Component
class PublicityMapper(private val userMapper: UserMapper)
{
    public fun toDomain(pub: PublicityEntity): Publicity{
        return Publicity(
            publicityId = pub.publicityId,
            user = userMapper.toDomain(pub.user),
            image = pub.image,
            title = pub.title,
            description = pub.description,
            isActive = pub.isActive,
            createdAt = pub.createdAt
        )
    }

    public fun toEntity(pub: Publicity): PublicityEntity{
        return PublicityEntity(
            publicityId = pub.publicityId,
            user = userMapper.toEntityToDto(pub.user),
            image = pub.image,
            title = pub.title,
            description = pub.description,
            isActive = pub.isActive,
            createdAt = pub.createdAt
        )
    }
}