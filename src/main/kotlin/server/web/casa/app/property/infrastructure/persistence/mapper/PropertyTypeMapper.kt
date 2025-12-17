package server.web.casa.app.property.infrastructure.persistence.mapper

import server.web.casa.app.property.domain.model.PropertyType
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyTypeEntity

fun PropertyTypeEntity.toDomain() = PropertyType(
    propertyTypeId = this.id!!,
    name = this.name,
    description = this.description
)

fun PropertyType.toEntity() = PropertyTypeEntity(
    id = this.propertyTypeId,
    name = this.name,
    description = this.description
)