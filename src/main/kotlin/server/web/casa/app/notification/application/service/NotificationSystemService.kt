package server.web.casa.app.notification.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.notification.domain.model.NotificationSystem
import server.web.casa.app.notification.infrastructure.persistence.entity.toDomain
import server.web.casa.app.notification.infrastructure.persistence.mapper.*
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationCasaRepository
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationSystemRepository

@Service
class NotificationSystemService(
    private val repository: NotificationSystemRepository,
    private val notification: NotificationCasaRepository,

) {
    suspend fun create(notice : NotificationSystem): NotificationSystem {
        val data = repository.save(notice.toEntity())
           return data.toDomain()
    }
    suspend fun notificationByUser(id : Long) = coroutineScope { notification.getAllByUser(id).map { it?.toDomain() }.toList() }
    suspend fun notificationDisable(id : Long) = coroutineScope {
        val data = notification.findById(id)?:throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Cette notification n'existe pas")
        data.isActive = false
        notification.save(data)
    }
}