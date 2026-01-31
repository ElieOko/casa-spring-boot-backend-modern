package server.web.casa.app.notification.domain.model

import jakarta.validation.constraints.NotNull
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationCasaEntity
import java.time.LocalDate
import java.time.LocalDateTime

data class NotificationSystem(
    val notificationSystemId : Long? = null,
    val title : String,
    val description : String,
    val dateCreated : LocalDate = LocalDate.now()
)
data class NotificationDTO(
    val id: Long? = null,
    val userId: Long? = null,
    val title: String,
    val isActive: Boolean = true,
    val message: String,
    val tag : String = TagType.RESERVATION.toString(),
    val timestamp: LocalDateTime = LocalDateTime.now()
)

data class NotificationRequest(
    @NotNull
    val title: String,
    @NotNull
    val message: String,
    @NotNull
    val tag : String = TagType.RESERVATION.toString(),
)
fun NotificationDTO.toEntity() = NotificationCasaEntity(
    id = this.id,
    userId = this.userId,
    title = this.title,
    isActive = this.isActive,
    message = this.message,
    tag = this.tag,
)

enum class TagType{
    FINANCES,
    SECURITY,
    PROMOTIONS,
    DEMANDES,
    SYSTEMS,
    RESERVATION,
    AGENDA,
    SESSION
}