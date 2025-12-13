package server.web.casa.app.address.infrastructure.persistence.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.address.infrastructure.persistence.entity.CommuneEntity

interface CommuneRepository : CoroutineCrudRepository<CommuneEntity,Long>