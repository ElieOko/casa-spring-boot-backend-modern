package server.web.casa.app.address.infrastructure.persistence.entity

import jakarta.persistence.*
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity

@Entity
@Table(name = "quartiers")
 class QuartierEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column("id")
    val quartierId  : Long = 0,
    @ManyToOne
    @JoinColumn("commune_id")
    val commune   : CommuneEntity? = null,
    @Column("name")
    val name        : String,
    @OneToMany(mappedBy = "quartier")
    val properties : List<PropertyEntity> = emptyList()
)
