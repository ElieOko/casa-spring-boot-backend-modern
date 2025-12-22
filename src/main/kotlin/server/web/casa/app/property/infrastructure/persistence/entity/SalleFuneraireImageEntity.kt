package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.property.domain.model.VacanceImage
import java.time.LocalDate
import java.time.LocalDateTime

@Table("salle_funeraire_images")
data class SalleFuneraireImageEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("salle_funeraire_id")
    val salleFuneraireId : Long,
    @Column("name")
    val name: String,
    @Column("path")
    val path: String?,
    @Column("is_available")
    var isAvailable: Boolean = true,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
