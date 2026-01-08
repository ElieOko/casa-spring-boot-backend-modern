package server.web.casa.app.property.infrastructure.persistence.entity.favorite

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("favorite_funeraires")
class FavoriteFuneraireEntity(
    @Column("id")
    val id          : Long,
    @Column("user_id")
    val userId      : Long,
    @Column("funeraire_id")
    val funeraireId : Long,
    @Column("created_at")
    val createdAt   : LocalDateTime = LocalDateTime.now()
)
