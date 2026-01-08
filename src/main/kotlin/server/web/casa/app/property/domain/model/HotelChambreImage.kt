package server.web.casa.app.property.domain.model

import server.web.casa.app.property.infrastructure.persistence.entity.HotelChambreImageEntity
import java.time.LocalDate

class HotelChambreImage(
    val id: Long? = null,
    val hotelChambreId: Long? = null,
    val name: String,
    val path: String?,
    var isAvailable: Boolean = true,
    val createdAt: LocalDate = LocalDate.now(),
)

fun HotelChambreImage.toEntity() = HotelChambreImageEntity(
    id = this.id,
    hotelChambreId = this.hotelChambreId,
    name = this.name,
    path = this.path,
    isAvailable = this.isAvailable,
    createdAt = this.createdAt,
)