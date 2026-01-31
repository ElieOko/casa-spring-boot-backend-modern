package server.web.casa.app.notification.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationCasaEntity
import server.web.casa.app.property.infrastructure.persistence.entity.AgenceEntity

interface NotificationCasaRepository : CoroutineCrudRepository<NotificationCasaEntity, Long>{
    @Query("SELECT * FROM notifications WHERE user_id = :userId AND is_active = true")
    suspend fun getAllByUser(userId: Long) : Flow<NotificationCasaEntity?>
}