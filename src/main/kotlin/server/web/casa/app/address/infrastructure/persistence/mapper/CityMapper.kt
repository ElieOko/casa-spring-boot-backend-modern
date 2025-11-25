package server.web.casa.app.address.infrastructure.persistence.mapper

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import server.web.casa.app.address.domain.model.City
import server.web.casa.app.address.infrastructure.persistence.entity.CityEntity
import server.web.casa.utils.Mode

@Component
@Profile(Mode.DEV)
class CityMapper(
    val countryMapper: CountryMapper
) {
    fun toDomain(cityEntity: CityEntity?) : City?{
        return if (cityEntity!=null){
            City(
                cityId = cityEntity.cityId,
                country = countryMapper.toDomain(cityEntity.country),
                name = cityEntity.name
            )
        }
        else{
            null
        }
    }
    fun toEntity(city: City?) : CityEntity?{
        return if (city != null){
            CityEntity(
                cityId = city.cityId,
                country = countryMapper.toEntity(city.country),
                name = city.name
            )
        }
        else{
            null
        }
    }
}