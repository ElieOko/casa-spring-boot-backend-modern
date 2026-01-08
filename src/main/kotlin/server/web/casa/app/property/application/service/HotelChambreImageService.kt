package server.web.casa.app.property.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.property.infrastructure.persistence.repository.HotelChambreImageRepository
import server.web.casa.utils.gcs.GcsService

@Service
class HotelChambreImageService(
    private val repository: HotelChambreImageRepository,
    private val gcsService: GcsService,
) {
}