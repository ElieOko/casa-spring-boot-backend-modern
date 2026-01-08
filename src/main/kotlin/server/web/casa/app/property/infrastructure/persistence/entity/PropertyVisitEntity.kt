package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("property_visits")
data class PropertyVisitEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("property_id")
    val propertyId: Long,
    @Column("user_id")
    val userId: Long,
    @Column("message")
    val message: String,
    @Column("visit_date")
    val visitDate: String,
    @Column("visit_hour")
    val visitHour: String,
    @Column("status")
    val status: String,
    @Column("ip_address")
    val ipAddress: String,
    @Column("cancellation_reason")
    val cancellationReason: String
)
