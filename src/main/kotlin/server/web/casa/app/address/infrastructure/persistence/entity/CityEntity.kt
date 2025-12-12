package server.web.casa.app.address.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "cities")
class CityEntity(
    @Id
    val cityId : Long = 0,
    val countryId : Long,
    val name : String,
)
