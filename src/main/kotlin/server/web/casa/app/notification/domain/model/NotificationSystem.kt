package server.web.casa.app.notification.domain.model

import java.time.LocalDate

data class NotificationSystem(
    val notificationSystemId : Long? = null,
    val title : String,
    val description : String,
    val dateCreated : LocalDate = LocalDate.now()
)
