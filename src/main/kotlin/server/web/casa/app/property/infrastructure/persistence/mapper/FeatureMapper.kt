package server.web.casa.app.property.infrastructure.persistence.mapper

import org.springframework.stereotype.Component
import server.web.casa.app.property.domain.model.Feature
import server.web.casa.app.property.infrastructure.persistence.entity.FeatureEntity

@Component
class FeatureMapper{
    fun toDomain(p : FeatureEntity): Feature {
        return Feature(
            featureId = p.featureId,
            name = p.name
        )
    }

    fun toEntity(p : Feature): FeatureEntity {
        return FeatureEntity(
            featureId = p.featureId,
            name = p.name
        )
    }
}