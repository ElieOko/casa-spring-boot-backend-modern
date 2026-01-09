package server.web.casa.app.notification.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.notification.domain.model.NotificationDTO
import server.web.casa.app.notification.domain.model.TagType
import java.time.LocalDateTime

@Table("notifications")
class NotificationCasaEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("user_id")
    val userId: Long? = null,
    @Column("title")
    val title: String,
    @Column("message")
    val message: String,
    @Column("tag")
    val tag : String = TagType.RESERVATION.toString(),
    @Column("created")
    val created: LocalDateTime = LocalDateTime.now()
)

fun NotificationCasaEntity.toDomain() = NotificationDTO(
    id = this.id,
    userId = this.userId,
    title = this.title,
    message = this.message,
    tag = this.tag,
    timestamp = this.created
)