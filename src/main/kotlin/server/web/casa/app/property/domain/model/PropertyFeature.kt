package server.web.casa.app.property.domain.model

import server.web.casa.app.property.infrastructure.persistence.entity.FeatureEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity

data class PropertyFeature(
    val propertyFeatureId : Long = 0,
    val property: Property? = null,
    val feature: Feature? = null,
)
