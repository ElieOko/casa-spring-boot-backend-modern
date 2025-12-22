package server.web.casa.app.property.domain.model

import java.time.LocalDate

class HotelChambreImage(
    val id: Long? = null,
    val hotelChambreId: Long? = null,
    val name: String,
    val path: String?,
    var isAvailable: Boolean = true,
    val createdAt: LocalDate = LocalDate.now(),
)