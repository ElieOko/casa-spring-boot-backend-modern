package server.web.casa.app.property.infrastructure.persistence.mapper

import org.springframework.stereotype.Component
import server.web.casa.app.property.domain.model.PropertyFeature
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyFeatureEntity

@Component
class PropertyFeatureMapper{
    fun toDomain(p : PropertyFeatureEntity): PropertyFeature {
        return PropertyFeature(
            propertyFeatureId = p.propertyFeatureId,
            name = p.name
           // property = p.property.stream().map { propertyMapper.toDomain(it) }.toList()
        )
    }

    fun toEntity(p : PropertyFeature): PropertyFeatureEntity {
        return PropertyFeatureEntity(
            propertyFeatureId = p.propertyFeatureId,
            name = p.name
          //  property = p.property.stream().map { propertyMapper.toEntity(it) }.toList()
        )
    }
}