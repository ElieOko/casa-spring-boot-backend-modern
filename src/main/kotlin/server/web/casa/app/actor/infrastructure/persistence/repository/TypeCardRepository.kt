package server.web.casa.app.actor.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.actor.infrastructure.persistence.entity.TypeCardEntity

interface TypeCardRepository : JpaRepository<TypeCardEntity, Long>{}