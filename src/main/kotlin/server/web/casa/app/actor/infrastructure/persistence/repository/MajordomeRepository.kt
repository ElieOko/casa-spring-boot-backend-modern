package server.web.casa.app.actor.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.actor.infrastructure.persistence.entity.second.MajordomeEntity

interface MajordomeRepository : JpaRepository<MajordomeEntity,Long>