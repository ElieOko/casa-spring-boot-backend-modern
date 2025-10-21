package server.web.casa.app.property.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity

@Entity
@Table(name = "features")
 class FeatureEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val featureId : Long = 0,
    @Column(name = "name")
    val name : String,
    @ManyToMany(mappedBy = "features",fetch = FetchType.LAZY)
    @JsonBackReference
    val property: MutableSet<PropertyEntity> = mutableSetOf()
)
