package server.web.casa.app.ecosystem.infrastructure.persistence.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.PrestataireServiceEntity

interface PrestataireServiceRepository : CoroutineCrudRepository<PrestataireServiceEntity, Long>