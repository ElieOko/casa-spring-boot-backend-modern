package server.web.casa.app.property.infrastructure.persistence.mapper

import server.web.casa.app.property.domain.model.Feature
import server.web.casa.app.property.infrastructure.persistence.entity.FeatureEntity

fun FeatureEntity.toDomain() = Feature(featureId = this.featureId, name = this.name)

fun Feature.toEntity() = FeatureEntity(featureId = this.featureId, name = this.name)
