package server.web.casa.app.property.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.property.infrastructure.persistence.repository.HotelChambreRepository

@Service
class HotelChambreService(
    private val repostory : HotelChambreRepository,
) {

}