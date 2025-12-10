package server.web.casa.app.ecosystem.infrastructure.persistence.repository.realisation

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.ajusteur.ServiceAjusteurRealisationEntity

interface AjusteurRealisationRepository: JpaRepository<ServiceAjusteurRealisationEntity, Long>