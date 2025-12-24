package server.web.casa.app.prestation.infrastructure.persistance.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("cotation_prestation")
data class CotationPrestationEntity(
    @Id
    @Column("id")
    val id              : Long? = null,
    @Column("user_id")
    val userId          : Long,
    @Column("sollicitation_id")
    val sollicitationId : Long,
    @Column("cote")
    val cote            : Float,
    @Column("commentaire")
    val commentaire     : String?,
    @Column("is_active")
    val isActive        : Boolean,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
