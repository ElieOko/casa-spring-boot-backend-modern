package server.web.casa.app.address.infrastructure.persistence.entity

import jakarta.persistence.*
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity

@Entity
@Table(name = "cities")
class CityEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column("id")
    val cityId : Long = 0,
    @OneToOne
    @JoinColumn("country_id")
    val country : CountryEntity,
    @Column("name")
    val name : String,
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
    val district: List<DistrictEntity> = emptyList(),
    @OneToMany(mappedBy = "city")
    val properties : List<PropertyEntity> = emptyList()
)
