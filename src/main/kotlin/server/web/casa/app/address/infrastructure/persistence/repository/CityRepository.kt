package server.web.casa.app.address.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.address.infrastructure.persistence.entity.CityEntity

interface CityRepository : JpaRepository<CityEntity, Long> {
}