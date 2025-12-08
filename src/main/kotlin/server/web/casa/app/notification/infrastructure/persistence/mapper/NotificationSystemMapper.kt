package server.web.casa.app.notification.infrastructure.persistence.mapper

import server.web.casa.app.notification.domain.model.NotificationSystem
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationSystemEntity

fun NotificationSystemEntity.toDomain() = NotificationSystem(
    notificationSystemId = this.notificationSystemId,
    title = this.title,
    description = this.description,
    dateCreated = this.dateCreated
)

fun NotificationSystem.toEntity()  = NotificationSystemEntity(
    notificationSystemId = this.notificationSystemId,
    title = this.title,
    description = this.description,
    dateCreated = this.dateCreated
)
