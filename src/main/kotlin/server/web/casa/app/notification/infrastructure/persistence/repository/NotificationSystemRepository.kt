package server.web.casa.app.notification.infrastructure.persistence.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationSystemEntity

interface NotificationSystemRepository : CoroutineCrudRepository<NotificationSystemEntity, Long>