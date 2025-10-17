package server.web.casa.app.property.infrastructure.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "PropertyTypes")
data class PropertyTypeEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val propertyTypeId : Long = 0,
    @Column(name = "name")
    val name : String,
    @Column(name = "description", nullable = true)
    val description : String? = "",
    @OneToMany(mappedBy = "propertyType")
    val property : List<PropertyEntity?> = emptyList(),
)