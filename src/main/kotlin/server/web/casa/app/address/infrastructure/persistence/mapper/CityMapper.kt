package server.web.casa.app.address.infrastructure.persistence.mapper

import server.web.casa.app.address.domain.model.City
import server.web.casa.app.address.infrastructure.persistence.entity.CityEntity

fun City.toEntity() = CityEntity(cityId = this.cityId, countryId = this.country, name = this.name)

fun CityEntity.toDomain() = City(cityId = this.cityId, country = this.countryId, name = this.name)
