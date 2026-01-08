package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("bureau_features")
data class BureauFeatureEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("bureau_id")
    val bureauId: Long,
    @Column("feature_id")
    val featureId: Long
)
