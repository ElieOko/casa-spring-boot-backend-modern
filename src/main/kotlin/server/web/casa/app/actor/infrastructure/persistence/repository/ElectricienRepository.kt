package server.web.casa.app.actor.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.actor.infrastructure.persistence.entity.ArchitecteEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.CarreleurEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.ChauffeurEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.ElectricienEntity

interface ElectricienRepository : JpaRepository<ElectricienEntity,Long>