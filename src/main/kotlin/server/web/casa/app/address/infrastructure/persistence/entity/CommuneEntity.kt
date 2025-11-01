package server.web.casa.app.address.infrastructure.persistence.entity

import jakarta.persistence.*
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity

@Entity
@Table(name = "communes")
class CommuneEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column("id")
    val communeId   : Long = 0,
    @ManyToOne
    @JoinColumn("district_id")
    val district  : DistrictEntity? = null,
    @Column("name")
    val name        : String,
    @OneToMany(mappedBy = "commune")
    val quartier : List<QuartierEntity> = emptyList(),
    @OneToMany(mappedBy = "commune")
    val properties : List<PropertyEntity> = emptyList()
)
