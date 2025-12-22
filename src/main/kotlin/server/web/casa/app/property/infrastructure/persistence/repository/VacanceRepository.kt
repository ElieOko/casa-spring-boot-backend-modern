package server.web.casa.app.property.infrastructure.persistence.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.VacanceEntity

interface VacanceRepository : CoroutineCrudRepository<VacanceEntity, Long>{

}