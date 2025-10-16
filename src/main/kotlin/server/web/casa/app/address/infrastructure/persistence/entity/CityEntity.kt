package server.web.casa.app.address.infrastructure.persistence.entity

import jakarta.persistence.*
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity

@Entity
@Table(name = "cities")
data class CityEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column("id")
    val cityId : Long = 0,
    @OneToOne
    @JoinColumn("country_id")
    val country : CountryEntity,
    @Column("name")
    val name : String,
    @OneToMany(mappedBy = "city", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val user: List<UserEntity> = emptyList(),
    @OneToMany(mappedBy = "city", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val district: List<DistrictEntity> = emptyList(),
    @OneToMany(mappedBy = "city")
    val properties : List<PropertyEntity> = emptyList()
)
