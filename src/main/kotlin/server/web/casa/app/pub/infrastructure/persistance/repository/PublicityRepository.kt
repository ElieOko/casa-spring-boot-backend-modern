package server.web.casa.app.pub.infrastructure.persistance.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.pub.infrastructure.persistance.entity.PublicityEntity

interface PublicityRepository : CoroutineCrudRepository<PublicityEntity, Long>
