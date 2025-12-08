package server.web.casa.app.property.infrastructure.persistence.mapper

import server.web.casa.app.property.domain.model.PropertyImageKitchen
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageKitchenEntity

fun PropertyImageKitchenEntity.toDomain() = PropertyImageKitchen(
        propertyImageKitchenId = this.propertyImageKitchenId,
        name = this.name,
        path = this.path
    )
