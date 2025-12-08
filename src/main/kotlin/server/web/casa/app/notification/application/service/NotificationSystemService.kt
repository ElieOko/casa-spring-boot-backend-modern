package server.web.casa.app.notification.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.notification.domain.model.NotificationSystem
import server.web.casa.app.notification.infrastructure.persistence.mapper.*
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationSystemRepository

@Service
class NotificationSystemService(
    private val repository: NotificationSystemRepository,
) {
    fun create(notice : NotificationSystem): NotificationSystem {
        val data = repository.save(notice.toEntity())
           return data.toDomain()
    }
}