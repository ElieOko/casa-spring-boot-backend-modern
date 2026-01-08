package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("property_image_rooms")
data class PropertyImageRoomEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("property_id")
    var propertyId: Long? = null,
    @Column("name")
    var name: String,
    @Column("path")
    var path: String
)
