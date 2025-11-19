package server.web.casa.app.address.infrastructure.persistence.mapper

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import server.web.casa.app.address.domain.model.Country
import server.web.casa.app.address.infrastructure.persistence.entity.CountryEntity
import server.web.casa.utils.Mode

@Component
@Profile(Mode.DEV)
class CountryMapper(
//    val cityMapper: CityMapper
) {
    fun toDomain(countryEntity: CountryEntity): Country{
        return Country(
            countryId = countryEntity.countryId,
            name = countryEntity.name
        )
    }
    fun toEntity(country: Country) : CountryEntity {
        return CountryEntity(
            countryId = country.countryId,
            name = country.name
        )
    }
}