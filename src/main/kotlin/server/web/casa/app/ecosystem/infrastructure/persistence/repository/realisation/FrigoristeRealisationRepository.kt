package server.web.casa.app.ecosystem.infrastructure.persistence.repository.realisation

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.frigoriste.ServiceFrigoristeRealisationEntity

interface FrigoristeRealisationRepository  : JpaRepository<ServiceFrigoristeRealisationEntity, Long>