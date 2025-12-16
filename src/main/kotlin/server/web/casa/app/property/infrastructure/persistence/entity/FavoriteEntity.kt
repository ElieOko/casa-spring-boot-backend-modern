package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table(name = "favorites")
class FavoriteEntity (
    @Id
    val id: Long = 0,
    val userId: Long?,
    val propertyId: Long?,
    val createdAt: LocalDate = LocalDate.now()
)