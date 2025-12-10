package server.web.casa.app.ecosystem.infrastructure.persistence.repository.realisation

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.salubrite.ServiceSalubriteRealisationEntity

interface SalubriteRealisationRepository : JpaRepository<ServiceSalubriteRealisationEntity, Long>