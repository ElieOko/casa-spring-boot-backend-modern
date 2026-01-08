package server.web.casa.app.property.infrastructure.persistence.entity.favorite

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("favorite_festives")
data class FavoriteFestiveEntity(
    @Column("id")
    val id          : Long,
    @Column("user_id")
    val userId      : Long,
    @Column("festive_id")
    val festiveId   : Long,
    @Column("created_at")
    val createdAt   : LocalDateTime = LocalDateTime.now()
)
