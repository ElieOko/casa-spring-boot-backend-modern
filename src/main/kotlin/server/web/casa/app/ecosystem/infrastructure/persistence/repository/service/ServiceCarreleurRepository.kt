package server.web.casa.app.ecosystem.infrastructure.persistence.repository.service

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.carreleur.ServiceCarreleurEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.carreleur.ServiceCarreleurRealisationEntity

interface ServiceCarreleurRepository : JpaRepository<ServiceCarreleurEntity, Long>