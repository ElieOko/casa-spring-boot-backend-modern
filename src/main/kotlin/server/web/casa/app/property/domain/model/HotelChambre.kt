package server.web.casa.app.property.domain.model

import org.jetbrains.annotations.NotNull
import server.web.casa.app.ecosystem.domain.request.ImageRequest
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

class HotelChambreRequest(
    @NotNull
    val userId: Long? = null,
    @NotNull
    var hotelId: Long? = null,
    @NotNull
    val deviseId: Long? = null,
    @NotNull
    val title: String,
    val description: String? = "",
    val numberPiece: Long? = null,
    @NotNull
    val price : Double? = null,
    val priceHeure : String? = "",
    var isAvailable: Boolean = true,
    val images : List<ImageRequest>,
)

fun HotelChambreRequest.toDomain() = HotelChambre(
    hotelId = hotelId,
    deviseId = deviseId,
    title = title,
    description = description,
    numberPiece = numberPiece,
    price = price,
    priceHeure = priceHeure,
    isAvailable = isAvailable,
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