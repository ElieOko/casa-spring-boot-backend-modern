package server.web.casa.app.notification.infrastructure.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.threeten.bp.LocalDateTime
import java.time.LocalDate

@Entity
@Table(name = "notification_systems")
class NotificationSystemEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column("id")
    val notificationSystemId : Long = 0,
    @Column("title")
    val title : String,
    @Column("description")
    val description : String,
    @Column("dateCreated")
    val dateCreated : LocalDate = LocalDate.now()
)