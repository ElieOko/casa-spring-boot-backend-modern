package server.web.casa.app.address.infrastructure.persistence.mapper

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import server.web.casa.app.address.domain.model.Quartier
import server.web.casa.app.address.infrastructure.persistence.entity.QuartierEntity
import server.web.casa.utils.Mode

@Component
@Profile(Mode.DEV)
class QuartierMapper(
//    val communeMapper: CommuneMapper
) {
    fun toDomain(quartierEntity: QuartierEntity): Quartier{
        return Quartier(
            quartierId = quartierEntity.quartierId,
//            commune = communeMapper.toDomain(quartierEntity.commune),
            name = quartierEntity.name
        )
    }
    fun toEntity(quartier: Quartier) : QuartierEntity{
        return QuartierEntity(
            quartierId = quartier.quartierId,
//            commune = communeMapper.toEntity(quartier.commune),
            name = quartier.name
        )
    }
}