package server.web.casa.app.actor.infrastructure.persistence.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.actor.domain.model.Bailleur
import server.web.casa.app.actor.infrastructure.persistence.entity.PersonEntity

interface BailleurRepository : CoroutineCrudRepository<Bailleur, Long> {}
