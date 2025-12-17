package server.web.casa.app.address.infrastructure.persistence.entity


import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "countries")
class CountryEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("name")
    val name: String
)
