package server.web.casa.app.ecosystem.infrastructure.persistence.repository.service

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.architect.ServiceArchitectEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.architect.ServiceArchitectRealisationEntity

interface ServiceArchitectRepository : JpaRepository<ServiceArchitectEntity, Long>