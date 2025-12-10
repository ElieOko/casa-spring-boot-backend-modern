package server.web.casa.app.payment.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.payment.infrastructure.persistence.entity.DeviseEntity

interface DeviseRepository : JpaRepository<DeviseEntity, Long>