package server.web.casa.app.property.domain.model.dto

import server.web.casa.app.property.domain.model.*

data class HotelChambreDTO(
    val chambre : HotelChambre,
    val image : List<HotelChambreImage>
)

data class HotelGlobal(
    val hotel: Hotel,
    val structure: List<HotelChambreDTO>,
    val image: String
)