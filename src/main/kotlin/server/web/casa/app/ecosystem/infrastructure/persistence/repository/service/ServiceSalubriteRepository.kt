package server.web.casa.app.ecosystem.infrastructure.persistence.repository.service

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.salubrite.ServiceSalubriteEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.salubrite.ServiceSalubriteRealisationEntity

interface ServiceSalubriteRepository : JpaRepository<ServiceSalubriteEntity, Long>