package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "PropertyFeatures")
 class PropertyFeatureEntity(
    @Id
    val id : Long = 0,
    val propertyId : Long,
    val featureId : Long,
)
