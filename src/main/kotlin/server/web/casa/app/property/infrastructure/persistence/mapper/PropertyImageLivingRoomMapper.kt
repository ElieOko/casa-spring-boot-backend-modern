package server.web.casa.app.property.infrastructure.persistence.mapper

import server.web.casa.app.property.domain.model.PropertyImageLivingRoom
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageLivingRoomEntity

fun PropertyImageLivingRoomEntity.toDomain() = PropertyImageLivingRoom(
        propertyImageLivingRoomId = this.propertyImageLivingRoomId,
        name = this.name,
        path = this.path
)
