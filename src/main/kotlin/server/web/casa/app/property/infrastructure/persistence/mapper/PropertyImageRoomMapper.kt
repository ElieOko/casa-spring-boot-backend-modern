package server.web.casa.app.property.infrastructure.persistence.mapper

import server.web.casa.app.property.domain.model.PropertyImageRoom
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageRoomEntity

fun PropertyImageRoomEntity.toDomain() = PropertyImageRoom(
        propertyImageRoomId = this.propertyImageRoomId,
        name = this.name,
        path = this.path
    )
