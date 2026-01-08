package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.property.domain.model.HotelChambre
import java.time.LocalDate

@Table(name = "hotel_chambres")
class HotelChambreEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("hotel_id")
    val hotelId: Long? = null,
    @Column("devise_id")
    val deviseId: Long? = null,
    @Column("title")
    val title: String,
    @Column("description")
    val description: String? = "",
    @Column("number_piece")
    val numberPiece: Long? = null,
    @Column("price")
    val price : Double? = null,
    @Column("price_heure")
    val priceHeure : String? = "",
    @Column("is_available")
    var isAvailable: Boolean = true,
    @Column("created_at")
    val createdAt: LocalDate = LocalDate.now(),
    @Column("updated_at")
    val updatedAt: LocalDate = LocalDate.now()
)

fun HotelChambreEntity.toDomain() = HotelChambre(
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