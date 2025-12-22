package server.web.casa.app.property.infrastructure.persistence.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.HotelEntity

interface HotelRepository : CoroutineCrudRepository<HotelEntity, Long>{

}