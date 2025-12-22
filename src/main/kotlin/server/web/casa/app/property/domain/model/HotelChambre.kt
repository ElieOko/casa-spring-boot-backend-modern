package server.web.casa.app.property.domain.model

import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table(name = "hotel_chambres")
class HotelChambre(
    val id: Long? = null,
    val hotelId: Long? = null,
    val title: String,
    val description: String? = "",
    val numberPiece: Long? = null,
    val price : Double? = null,
    val priceHeure : String? = "",
    var isAvailable: Boolean = true,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now()
)