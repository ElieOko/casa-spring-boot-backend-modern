package server.web.casa.app.address.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "cities")
class CityEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("country_id")
    val countryId: Long,
    @Column("name")
    val name: String
)
