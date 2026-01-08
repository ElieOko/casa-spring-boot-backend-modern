package server.web.casa.app.property.domain.model

import server.web.casa.app.property.infrastructure.persistence.entity.HotelChambreEntity
import java.time.LocalDate

class HotelChambre(
    val id: Long? = null,
    val hotelId: Long? = null,
    val deviseId: Long? = null,
    val title: String,
    val description: String? = "",
    val numberPiece: Long? = null,
    val price : Double? = null,
    val priceHeure : String? = "",
    var isAvailable: Boolean = true,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now()
)

fun HotelChambre.toEntity() = HotelChambreEntity(
    id = this.id,
    hotelId = this.hotelId,
    deviseId = this.deviseId,
    title = this.title,
    description = this.description,
    numberPiece = this.numberPiece,
    price = this.price,
    priceHeure = this.priceHeure,
    isAvailable = this.isAvailable,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)