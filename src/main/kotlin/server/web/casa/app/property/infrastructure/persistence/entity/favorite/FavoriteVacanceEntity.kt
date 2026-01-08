package server.web.casa.app.property.infrastructure.persistence.entity.favorite

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("favorite_vacances")
data class FavoriteVacanceEntity(
    @Id
    @Column("id")
    val id          : Long,
    @Column("user_id")
    val userId      : Long,
    @Column("vacance_id")
    val vacanceId   : Long,
    @Column("created_at")
    val createdAt   : LocalDateTime = LocalDateTime.now()
)
