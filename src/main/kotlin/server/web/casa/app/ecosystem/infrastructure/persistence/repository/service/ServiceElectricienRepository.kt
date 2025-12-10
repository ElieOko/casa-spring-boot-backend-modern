package server.web.casa.app.ecosystem.infrastructure.persistence.repository.service

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.electricien.ServiceElectricienEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.electricien.ServiceElectricienRealisationEntity

interface ServiceElectricienRepository : JpaRepository<ServiceElectricienEntity, Long>