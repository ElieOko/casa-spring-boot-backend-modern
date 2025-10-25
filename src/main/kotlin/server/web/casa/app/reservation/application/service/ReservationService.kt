package server.web.casa.app.reservation.application.service

import jakarta.transaction.Transactional
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.reservation.domain.model.Reservation
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.mapper.ReservationMapper
import server.web.casa.app.reservation.infrastructure.persistence.repository.ReservationRepository
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import server.web.casa.utils.Mode
import java.time.LocalDate

@Service
@Profile(Mode.DEV)
class ReservationService(
    private val mapperR: ReservationMapper,
    private val repoR: ReservationRepository
) {
    suspend fun createReservation(reservation: Reservation): Reservation {
        val data = mapperR.toEntity(reservation)
        val result = repoR.save(data)
        return mapperR.toDomain(result)
    }

    suspend fun findAllReservation() : List<Reservation> {
        return repoR.findAll().map { mapperR.toDomain(it) }.toList()
    }

    suspend fun findId(id: Long): Reservation? {
      return repoR.findById(id).let { mapperR.toDomain(it.orElse(null)) }
    }

    suspend fun findByProperty(property: PropertyEntity): List<Reservation>? {
        return repoR.findByProperty(property)?.map {
            mapperR.toDomain(it)
        }?.toList()
    }
    suspend fun findByUser(user: UserEntity): List<Reservation>?{
        return repoR.findByUser(user).let {list-> list?.map{mapperR.toDomain(it)}?.toList() ?: emptyList() }
    }
    suspend fun findByStatus(status: ReservationStatus): List<Reservation>{
        return repoR.findAllByStatus(status).let {list-> list?.map{ mapperR.toDomain(it) }?.toList() ?: emptyList() }
    }
    suspend fun findByMonth(month: Int, year: Int): List<Reservation>{
        return repoR.findAllByMonthAndYear(month, year).let { list-> list?.map{ mapperR.toDomain(it) }?.toList() ?: emptyList() }
    }
    suspend fun findByPYear(year : Int) : List<Reservation>{
      return repoR.findAllByYear( year)?.map{ mapperR.toDomain(it) }?.toList() ?: emptyList()
    }
    suspend fun findByDate(inputDate: LocalDate): List<Reservation> {
        return repoR.findAllByDate(inputDate).map{ mapperR.toDomain(it) }.toList()
    }
    suspend fun findByInterval(starDate : LocalDate, endDate: LocalDate): List<Reservation>? {
      return repoR.findAllInInterval(starDate, endDate)?.map { mapperR.toDomain(it) }?.toList()
    }
        //update
        @Transactional
        suspend fun updateStatusById(id: Long, status: ReservationStatus):Int{
          return repoR.updateStatusById(id, status)
        }

    //delete
    @Transactional
    suspend fun deleteReservationById(id: Long):Int{
        val success = repoR.deleteByIdReservation(id)
        return success
    }
}