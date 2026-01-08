package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("funeraire_features")
data class FuneraireFeatureEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("funeraire_id")
    val funeraireId: Long,
    @Column("feature_id")
    val featureId: Long
)
