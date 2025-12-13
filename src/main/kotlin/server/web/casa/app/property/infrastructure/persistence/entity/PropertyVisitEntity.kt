package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "PropertyVisits")
class PropertyVisitEntity(
    @Id
    val id                  : Long,
    val propertyId          : Long,
    val userId              : Long,
    val message             : String,
    val visitDate           : String,
    val visitHour           : String,
    val status              : String,
    val ipAddress           : String,
    val cancellationReason  : String
)
