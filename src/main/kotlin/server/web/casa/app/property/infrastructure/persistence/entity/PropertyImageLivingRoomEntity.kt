package server.web.casa.app.property.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table


@Entity
@Table(name = "property_image_living_rooms")
class PropertyImageLivingRoomEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val propertyImageLivingRoomId : Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn("property_id")
    @JsonBackReference
    var propertyLiving : PropertyEntity,
    @Column(name = "name")
    val name : String,
    @Column(name = "path_image")
    val path : String
)
