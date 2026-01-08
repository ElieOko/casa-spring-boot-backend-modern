package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate


@Table("favorites")
data class FavoriteEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("user_id")
    val userId: Long? = null,
    @Column("property_id")
    val propertyId: Long? = null,
    @Column("created_at")
    val createdAt: LocalDate = LocalDate.now()
)