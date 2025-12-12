package server.web.casa.app.address.infrastructure.persistence.entity


import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "countries")
class CountryEntity(
    @Id
    val countryId : Long,
    val name : String,
)
