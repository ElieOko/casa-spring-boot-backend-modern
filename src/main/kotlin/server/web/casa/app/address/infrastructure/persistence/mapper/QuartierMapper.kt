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
    fun toDomain(quartierEntity: QuartierEntity?): Quartier?{
        return if (quartierEntity != null){
            Quartier(
                quartierId = quartierEntity.quartierId,
                name = quartierEntity.name
            )
        } else{
            null
        }
    }
    fun toEntity(quartier: Quartier?) : QuartierEntity?{
        return if (quartier != null){
            QuartierEntity(
                quartierId = quartier.quartierId,
                name = quartier.name
            )
        } else{
            null
        }
    }
}