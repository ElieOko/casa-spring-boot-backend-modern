package server.web.casa.app.property.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.CascadeType
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
@Table(name = "property_image_rooms")
class PropertyImageRoomEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val propertyImageRoomId : Long = 0,
    @ManyToOne()
    @JoinColumn("property_id")
    @JsonBackReference
    var propertyRoom : PropertyEntity?,
    @Column(name = "name")
    val name : String,
    @Column(name = "path_image")
    val path : String
)
