package server.web.casa.app.ecosystem.infrastructure.persistence.repository.service

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.frigoriste.ServiceFrigoristeEntity

interface ServiceFrigoristeRepository  : JpaRepository<ServiceFrigoristeEntity, Long>