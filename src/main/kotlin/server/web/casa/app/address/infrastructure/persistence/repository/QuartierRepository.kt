package server.web.casa.app.address.infrastructure.persistence.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.address.infrastructure.persistence.entity.QuartierEntity

interface QuartierRepository : CoroutineCrudRepository<QuartierEntity,Long>