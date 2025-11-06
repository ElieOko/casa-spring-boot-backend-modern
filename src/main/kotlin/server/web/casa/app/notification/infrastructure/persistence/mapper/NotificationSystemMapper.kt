package server.web.casa.app.notification.infrastructure.persistence.mapper

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import server.web.casa.app.notification.domain.model.NotificationSystem
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationSystemEntity
import server.web.casa.utils.Mode

@Component
@Profile(Mode.DEV)
class NotificationSystemMapper {

    fun toDomain(notice: NotificationSystemEntity): NotificationSystem{
        return NotificationSystem(
            notificationSystemId = notice.notificationSystemId,
            title = notice.title,
            description = notice.description,
            dateCreated = notice.dateCreated
        )
    }
    fun toEntity(notice: NotificationSystem) : NotificationSystemEntity{
        return NotificationSystemEntity(
            notificationSystemId = notice.notificationSystemId,
            title = notice.title,
            description = notice.description,
            dateCreated = notice.dateCreated
        )
    }
}