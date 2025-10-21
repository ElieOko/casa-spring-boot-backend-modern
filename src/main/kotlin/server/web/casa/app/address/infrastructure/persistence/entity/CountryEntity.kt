package server.web.casa.app.address.infrastructure.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "countries")
class CountryEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column("id")
    val countryId : Long,
    @Column("name")
    val name : String,
    @OneToOne(mappedBy = "country")
    val city : CityEntity? = null,

)
