package server.web.casa.app.property.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("property_images")
data class PropertyImageEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("property_id")
    var propertyId: Long? = null,
    @Column("name")
    val name: String,
    @Column("path")
    val path: String
)
