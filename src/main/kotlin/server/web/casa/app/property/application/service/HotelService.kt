package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.Hotel
import server.web.casa.app.property.domain.model.toEntity
import server.web.casa.app.property.infrastructure.persistence.entity.toDomain
import server.web.casa.app.property.infrastructure.persistence.repository.HotelRepository

@Service
class HotelService(
    private val hotelRepository: HotelRepository
) {
    suspend fun save(domain : Hotel) = coroutineScope {
        try {
            hotelRepository.save(domain.toEntity())
        } catch (e: Exception) {
            throw Exception(e.message, e)
        }
    }
    suspend fun findAll() = coroutineScope { hotelRepository.findAll().map { it.toDomain() }.toList() }
}