package server.web.casa.app.property.infrastructure.persistence.entity

import jakarta.persistence.*
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import java.time.LocalDate

@Table(name = "favorites")
@Entity
class FavoriteEntity (
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val favoriteId : Long = 0,

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    val user : UserEntity,

    @ManyToOne(optional = true)
    @JoinColumn(name = "property_id", nullable = true)
    val property : PropertyEntity ?,

    @ManyToOne(optional = true)
    @JoinColumn(name = "feature_id", nullable = true)
    val feature : FeatureEntity  ?,

    @Column(name = "createdAt")
    val createdAt: LocalDate = LocalDate.now()
)