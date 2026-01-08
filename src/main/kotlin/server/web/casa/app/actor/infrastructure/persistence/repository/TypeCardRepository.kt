package server.web.casa.app.actor.infrastructure.persistence.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.actor.infrastructure.persistence.entity.TypeCardEntity

interface TypeCardRepository : CoroutineCrudRepository<TypeCardEntity, Long>