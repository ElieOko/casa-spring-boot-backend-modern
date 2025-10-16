package server.web.casa.app.reservation.application.service

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.reservation.domain.model.Reservation
import server.web.casa.app.reservation.infrastructure.persistence.mapper.ReservationMapper
import server.web.casa.app.reservation.infrastructure.persistence.repository.ReservationRepository
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class ReservationService(
    private val mapperR: ReservationMapper,
    private val repoR: ReservationRepository
) {
    fun createReservation(reservation: Reservation): Reservation {
        val data = mapperR.toEntity(reservation)
        val result = repoR.save(data)
        return mapperR.toDomain(result)
    }

    fun findAllReservation() : List<Reservation> {
        val allEntity = repoR.findAll()
        return allEntity.stream().map {
            mapperR.toDomain(it)
        }.toList()
    }

    fun findId(id: Long): Reservation {
        val entity = repoR.findById(id)
            .orElseThrow { RuntimeException("Reservation with id $id not found") }
        return mapperR.toDomain(entity)
    }
}