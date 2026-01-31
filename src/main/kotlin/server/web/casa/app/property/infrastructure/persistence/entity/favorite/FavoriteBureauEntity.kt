package server.web.casa.app.property.infrastructure.persistence.entity.favorite

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("bureau_favorites")
class FavoriteBureauEntity(
    @Id
    @Column("id")
    val id          : Long? = null,
    @Column("user_id")
    val userId      : Long,
    @Column("bureau_id")
    val bureauId   : Long,
    @Column("created_at")
    val createdAt   : LocalDate = LocalDate.now()
)
