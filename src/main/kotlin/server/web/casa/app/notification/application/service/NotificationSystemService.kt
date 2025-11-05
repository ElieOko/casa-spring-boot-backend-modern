package server.web.casa.app.notification.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.notification.domain.model.NotificationSystem
import server.web.casa.app.notification.infrastructure.persistence.mapper.NotificationSystemMapper
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationSystemRepository

@Service
class NotificationSystemService(
    private val repository: NotificationSystemRepository,
    private val mapper: NotificationSystemMapper
) {
    fun create(notice : NotificationSystem): NotificationSystem {
        val data = repository.save(mapper.toEntity(notice))
           return mapper.toDomain(data)
    }
}