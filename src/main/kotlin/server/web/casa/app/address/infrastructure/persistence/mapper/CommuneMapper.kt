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
    val quartierMapper: QuartierMapper
) {
    fun toDomain(communeEntity: CommuneEntity) : Commune{
        return Commune(
            communeId = communeEntity.communeId,
//            quartiers = communeEntity.quartier.map { quartierMapper.toDomain(it) }.toList() ,
//            district = districtMapper.toDomain(communeEntity.district),
            name = communeEntity.name
        )
    }
    fun toDomainOrigin(communeEntity: CommuneEntity) : Commune{
        return Commune(
            communeId = communeEntity.communeId,
            quartiers = communeEntity.quartier.map { quartierMapper.toDomain(it) }.toList() ,
            district = districtMapper.toDomain(communeEntity.district),
            name = communeEntity.name
        )
    }

    fun toEntity(commune: Commune) : CommuneEntity{
        return CommuneEntity(
            communeId = commune.communeId,
            district = districtMapper.toEntity(commune.district),
            name = commune.name
        )
    }
}