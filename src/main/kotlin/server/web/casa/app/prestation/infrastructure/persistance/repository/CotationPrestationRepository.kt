package server.web.casa.app.prestation.infrastructure.persistance.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.prestation.infrastructure.persistance.entity.CotationPrestationEntity
import server.web.casa.app.prestation.infrastructure.persistance.entity.SollicitationEntity

interface CotationPrestationRepository : CoroutineCrudRepository<CotationPrestationEntity, Long> {}