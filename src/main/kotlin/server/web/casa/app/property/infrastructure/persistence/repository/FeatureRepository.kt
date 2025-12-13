package server.web.casa.app.property.infrastructure.persistence.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.FeatureEntity

interface FeatureRepository : CoroutineCrudRepository<FeatureEntity, Long>