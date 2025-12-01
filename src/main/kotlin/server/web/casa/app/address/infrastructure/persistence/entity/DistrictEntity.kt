package server.web.casa.app.address.infrastructure.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "districts")
class DistrictEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column("id")
    val districtId  : Long = 0,
    @ManyToOne(optional = true)
    @JoinColumn("city_id")
    val city      : CityEntity?,
    @Column("name")
    val name        : String,
    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
    val commune: List<CommuneEntity> = emptyList()
)
