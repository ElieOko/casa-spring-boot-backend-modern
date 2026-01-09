package server.web.casa.app.notification.infrastructure.persistence.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationCasaEntity
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationReservationEntity

interface NotificationCasaEntityRepository : CoroutineCrudRepository<NotificationCasaEntity, Long>{

}