package server.web.casa.app.ecosystem.infrastructure.persistence.repository.service

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.macon.ServiceMaconEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.macon.ServiceMaconRealisationEntity

interface ServiceMaconRepository : JpaRepository<ServiceMaconEntity, Long>