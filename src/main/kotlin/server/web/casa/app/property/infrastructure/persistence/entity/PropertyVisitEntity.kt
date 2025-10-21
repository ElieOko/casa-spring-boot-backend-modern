package server.web.casa.app.property.infrastructure.persistence.entity

import jakarta.persistence.*

@Table(name = "PropertyVisits")
@Entity
class PropertyVisitEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val propertyVisitId     : Long,
    @Column(name = "propertyId")
    val propertyId          : Int,
    @Column(name = "userId")
    val userId              : Int,
    @Column(name = "message")
    val message             : String,
    @Column(name = "visitDate")
    val visitDate           : String,
    @Column(name = "visitHour")
    val visitHour           : String,
    @Column(name = "status")
    val status              : String,
    @Column(name = "ipAddress")
    val ipAddress           : String,
    @Column(name = "cancellationReason")
    val cancellationReason  : String
)
