package server.web.casa.app.notification.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationSystemEntity

interface NotificationSystemRepository : JpaRepository<NotificationSystemEntity, Long>