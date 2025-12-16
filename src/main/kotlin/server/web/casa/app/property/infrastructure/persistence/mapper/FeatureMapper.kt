package server.web.casa.app.property.infrastructure.persistence.mapper

import server.web.casa.app.property.domain.model.Feature
import server.web.casa.app.property.infrastructure.persistence.entity.FeatureEntity

fun FeatureEntity.toDomain() = Feature(featureId = this.id, name = this.name)

fun Feature.toEntity() = FeatureEntity(id = this.featureId, name = this.name)
