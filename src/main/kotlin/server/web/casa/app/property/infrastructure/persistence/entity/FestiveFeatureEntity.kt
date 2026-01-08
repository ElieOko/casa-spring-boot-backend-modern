package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("festive_features")
data class FestiveFeatureEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("festive_id")
    val festiveId: Long,
    @Column("feature_id")
    val featureId: Long
)
