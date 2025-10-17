package server.web.casa.app.reservation.infrastructure.persistence.repository
import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity


interface ReservationRepository : JpaRepository<ReservationEntity, Long>{}
