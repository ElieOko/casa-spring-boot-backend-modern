package server.web.casa.app.property.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "property_image_rooms")
class PropertyImageRoomEntity(
    @Id
    val id : Long = 0,
    var propertyRoom : PropertyEntity?,
    val name : String,
    val path : String
)
