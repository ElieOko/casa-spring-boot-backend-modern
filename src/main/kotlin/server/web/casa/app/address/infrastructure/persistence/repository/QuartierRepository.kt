package server.web.casa.app.address.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.address.infrastructure.persistence.entity.QuartierEntity

interface QuartierRepository : JpaRepository<QuartierEntity,Long> {
}