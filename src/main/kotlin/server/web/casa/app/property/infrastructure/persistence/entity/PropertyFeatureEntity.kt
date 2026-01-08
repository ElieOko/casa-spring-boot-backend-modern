package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("property_features")
data class PropertyFeatureEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("property_id")
    val propertyId: Long,
    @Column("feature_id")
    val featureId: Long
)
