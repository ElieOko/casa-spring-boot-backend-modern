package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table(name = "PropertyTypes")
class PropertyTypeEntity(
    @Id
    val id : Long = 0,
    val name : String,
    val description : String? = "",
)