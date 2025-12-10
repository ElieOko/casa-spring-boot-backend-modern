package server.web.casa.app.ecosystem.infrastructure.persistence.repository.realisation

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.carreleur.ServiceCarreleurRealisationEntity

interface CarreleurRealisationRepository : JpaRepository<ServiceCarreleurRealisationEntity, Long>