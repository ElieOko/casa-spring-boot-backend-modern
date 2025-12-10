package server.web.casa.app.ecosystem.infrastructure.persistence.repository.service

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.menusier.ServiceMenusierEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.menusier.ServiceMenusierRealisationEntity

interface ServiceMenusierRepository : JpaRepository<ServiceMenusierEntity, Long>