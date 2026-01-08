package server.web.casa.app.payment.infrastructure.persistence.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.payment.infrastructure.persistence.entity.DeviseEntity

interface DeviseRepository : CoroutineCrudRepository<DeviseEntity, Long>