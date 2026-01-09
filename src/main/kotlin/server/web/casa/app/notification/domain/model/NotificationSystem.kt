package server.web.casa.app.notification.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class NotificationSystem(
    val notificationSystemId : Long? = null,
    val title : String,
    val description : String,
    val dateCreated : LocalDate = LocalDate.now()
)
data class NotificationDTO(
    val id: Long = 0,
    val title: String,
    val message: String,
    val tag : String = TagType.RESERVATION.toString(),
    val timestamp: LocalDateTime = LocalDateTime.now()
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