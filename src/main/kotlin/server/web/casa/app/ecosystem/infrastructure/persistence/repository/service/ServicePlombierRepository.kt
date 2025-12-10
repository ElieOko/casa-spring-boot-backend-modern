package server.web.casa.app.ecosystem.infrastructure.persistence.repository.service

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.plombier.ServicePlombierEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.plombier.ServicePlombierRealisationEntity

interface ServicePlombierRepository : JpaRepository<ServicePlombierEntity, Long>