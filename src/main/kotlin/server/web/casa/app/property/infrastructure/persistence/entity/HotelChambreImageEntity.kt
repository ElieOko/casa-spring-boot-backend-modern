package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table(name = "hotel_chambre_images")
class HotelChambreImageEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("hotel_chambre_id")
    val hotelChambreId: Long? = null,
    @Column("name")
    val name: String,
    @Column("path")
    val path: String?,
    @Column("is_available")
    var isAvailable: Boolean = true,
    @Column("created_at")
    val createdAt: LocalDate = LocalDate.now(),
)