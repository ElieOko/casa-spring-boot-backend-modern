package server.web.casa.app.property.infrastructure.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "features")
data class FeatureEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val featureId : Long = 0,
    @Column(name = "name")
    val name : String,
)
