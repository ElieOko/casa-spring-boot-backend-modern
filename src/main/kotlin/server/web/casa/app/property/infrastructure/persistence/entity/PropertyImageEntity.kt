package server.web.casa.app.property.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(name = "PropertyImages")
class PropertyImageEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val propertyImageId : Long = 0,
    @ManyToOne(cascade = [CascadeType.ALL],fetch = FetchType.LAZY)
    @JoinColumn("property_id")
    @JsonBackReference
    var property : PropertyEntity,
    @Column(name = "name")
    val name : String,
    @Column(name = "path_image")
    val path : String
)
