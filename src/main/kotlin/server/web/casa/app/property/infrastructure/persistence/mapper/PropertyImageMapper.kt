package server.web.casa.app.property.infrastructure.persistence.mapper

import server.web.casa.app.property.domain.model.PropertyImage
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageEntity

fun PropertyImageEntity.toDomain()  = PropertyImage(
        propertyImageId = this.propertyImageId,
        name = this.name,
        path = this.path
)