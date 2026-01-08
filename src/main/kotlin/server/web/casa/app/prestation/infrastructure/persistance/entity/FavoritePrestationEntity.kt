package server.web.casa.app.prestation.infrastructure.persistance.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("favorite_prestations")
data class FavoritePrestationEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("user_id")
    val userId: Long?,
    @Column("prestation_id")
    val prestationId: Long?,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()

)
