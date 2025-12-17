package server.web.casa.app.address.infrastructure.persistence.mapper

import server.web.casa.app.address.domain.model.Country
import server.web.casa.app.address.infrastructure.persistence.entity.CountryEntity

fun CountryEntity.toDomain() = Country( countryId = this.id, name = this.name)

fun Country.toEntity()  = CountryEntity( id = this.countryId, name = this.name)