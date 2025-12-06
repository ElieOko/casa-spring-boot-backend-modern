package server.web.casa.app.actor.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.actor.infrastructure.persistence.entity.PeintreEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.PlombierEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.SalubriteEntity

interface SalubriteRepository : JpaRepository<SalubriteEntity,Long>