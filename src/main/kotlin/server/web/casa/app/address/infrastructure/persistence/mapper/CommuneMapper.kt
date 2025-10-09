package server.web.casa.app.address.infrastructure.persistence.mapper

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import server.web.casa.app.address.domain.model.Commune
import server.web.casa.app.address.infrastructure.persistence.entity.CommuneEntity
import server.web.casa.utils.Mode

@Component
@Profile(Mode.DEV)
class CommuneMapper(
    val districtMapper: DistrictMapper,
//    val quartierMapper: QuartierMapper
) {
    fun toDomain(communeEntity: CommuneEntity) : Commune{
        return Commune(
            communeId = communeEntity.communeId,
            district = districtMapper.toDomain(communeEntity.district),
            name = communeEntity.name,
//            quartier = quartierMapper.toDomain(communeEntity.quartier)
        )
    }
    fun toEntity(commune: Commune) : CommuneEntity{
        return CommuneEntity(
            communeId = commune.communeId,
            district = districtMapper.toEntity(commune.district),
            name = commune.name,
//            quartier = quartierMapper.toEntity(commune.quartier)
        )
    }
}