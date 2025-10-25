package server.web.casa.app.property.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageKitchenEntity

interface PropertyImageKitchenRepository : JpaRepository<PropertyImageKitchenEntity, Long> {
}