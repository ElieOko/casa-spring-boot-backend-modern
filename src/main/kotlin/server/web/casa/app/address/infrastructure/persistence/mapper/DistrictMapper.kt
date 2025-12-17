package server.web.casa.app.address.infrastructure.persistence.mapper

import server.web.casa.app.address.domain.model.District
import server.web.casa.app.address.infrastructure.persistence.entity.DistrictEntity

fun District.toEntity() = DistrictEntity(districtId = this.districtId, cityId = this.city, name = this.name)

fun DistrictEntity.toDomain() = District( districtId = this.districtId, city = this.cityId, name = this.name)