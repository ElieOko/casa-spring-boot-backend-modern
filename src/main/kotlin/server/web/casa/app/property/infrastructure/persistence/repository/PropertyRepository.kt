package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity

interface PropertyRepository : CoroutineCrudRepository<PropertyEntity, Long> {
    @EntityGraph(attributePaths = [
        "propertyImage",
        "propertyImageRoom",
        "propertyImageLivingRoom",
        "propertyImageKitchen"])
    override fun findAll(): Flow<PropertyEntity>
}
