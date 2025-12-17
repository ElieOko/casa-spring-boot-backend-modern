package server.web.casa.app.address.infrastructure.persistence.mapper

import server.web.casa.app.address.domain.model.Commune
import server.web.casa.app.address.infrastructure.persistence.entity.CommuneEntity

fun CommuneEntity.toDomain() = Commune( communeId = this.communeId, name = this.name)

fun CommuneEntity.toDomainOrigin() = Commune(
    communeId = this.communeId,
//    quartiers = this.,
    district = this.districtId,
    name = this.name
)

fun Commune.toEntity() = CommuneEntity(
    communeId = this.communeId,
    districtId = this.district,
    name = this.name
)