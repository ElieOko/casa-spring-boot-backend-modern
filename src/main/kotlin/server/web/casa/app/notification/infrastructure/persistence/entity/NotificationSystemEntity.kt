package server.web.casa.app.notification.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table(name = "notification_systems")
class NotificationSystemEntity(
    @Id
    val id : Long = 0,
    val title : String,
    val description : String,
    val dateCreated : LocalDate = LocalDate.now()
)