package server.web.casa.app.address.infrastructure.persistence.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.address.infrastructure.persistence.entity.CityEntity

interface CityRepository : CoroutineCrudRepository<CityEntity, Long>