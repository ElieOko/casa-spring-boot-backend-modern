package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "PropertyTypes")
class PropertyTypeEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("name")
    val name: String,
    @Column("description")
    val description: String? = ""
)