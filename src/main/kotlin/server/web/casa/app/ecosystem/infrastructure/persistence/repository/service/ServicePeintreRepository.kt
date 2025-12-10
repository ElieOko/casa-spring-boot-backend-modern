package server.web.casa.app.ecosystem.infrastructure.persistence.repository.service

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.peintre.ServicePeintreEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.peintre.ServicePeintreRealisationEntity

interface ServicePeintreRepository : JpaRepository<ServicePeintreEntity, Long>