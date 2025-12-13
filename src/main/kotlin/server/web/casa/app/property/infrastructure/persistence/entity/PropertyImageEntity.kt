package server.web.casa.app.property.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "PropertyImages")
class PropertyImageEntity(
    @Id
    val id : Long = 0,
    @JsonBackReference
    var propertyId : Long,
    val name : String,
    val path : String
)
