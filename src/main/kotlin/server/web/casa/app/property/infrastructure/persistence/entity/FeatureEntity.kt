package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "features")
 class FeatureEntity(
    @Id
    val id : Long = 0,
    val name : String,
)
