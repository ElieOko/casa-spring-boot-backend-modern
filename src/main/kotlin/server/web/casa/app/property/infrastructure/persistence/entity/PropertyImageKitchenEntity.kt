package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table(name = "property_image_kitchens")
class PropertyImageKitchenEntity(
    @Id
    val id : Long = 0,
    var propertyId : Long,
    val name : String,
    val path : String
)
