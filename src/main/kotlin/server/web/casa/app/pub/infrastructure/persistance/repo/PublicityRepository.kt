package server.web.casa.app.pub.infrastructure.persistance.repo

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.pub.infrastructure.persistance.entity.PublicityEntity

interface PublicityRepository : JpaRepository<PublicityEntity, Long>{}
