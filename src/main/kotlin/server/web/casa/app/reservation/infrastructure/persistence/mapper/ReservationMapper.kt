package server.web.casa.app.reservation.infrastructure.persistence.mapper

import server.web.casa.app.property.infrastructure.persistence.mapper.toDomain
import server.web.casa.app.property.infrastructure.persistence.mapper.toEntity
import server.web.casa.app.reservation.domain.model.Reservation
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import server.web.casa.app.user.infrastructure.persistence.mapper.toDomain
import server.web.casa.app.user.infrastructure.persistence.mapper.toEntityToDto

fun ReservationEntity.toDomain() = Reservation(
    reservationId = this.id,
    user = this.userId,
    message = this.message,
    status = this.status,
    type = this.type,
    isActive = this.isActive,
    reservationHeure = this.reservationHeure,
    cancellationReason = this.cancellationReason,
    startDate = this.startDate,
    endDate = this.endDate,
    createdAt = this.createdAt,
    property = this.propertyId
)

fun Reservation.toEntity() = ReservationEntity(
    id = this.reservationId,
    userId = this.user,
    propertyId = this.property,
    message = this.message,
    status = this.status,
    type = this.type,
    isActive = this.isActive,
    reservationHeure = this.reservationHeure,
    cancellationReason = this.cancellationReason,
    startDate = this.startDate,
    endDate = this.endDate,
    createdAt = this.createdAt
)