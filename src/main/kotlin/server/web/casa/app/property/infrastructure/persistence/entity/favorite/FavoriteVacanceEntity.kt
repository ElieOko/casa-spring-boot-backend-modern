package server.web.casa.app.property.infrastructure.persistence.entity.favorite

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("favorite_vacances")
class FavoriteVacanceEntity(
    @Id
    @Column("id")
    val id          : Long?=null,
    @Column("user_id")
    val userId      : Long,
    @Column("vacance_id")
    val vacanceId   : Long,
    @Column("created_at")
    val createdAt   : LocalDate = LocalDate.now()
)
