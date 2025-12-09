package server.web.casa.app.reservation.application.service

import jakarta.transaction.Transactional
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.reservation.domain.model.Reservation
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.mapper.*
import server.web.casa.app.reservation.infrastructure.persistence.repository.ReservationRepository
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import server.web.casa.utils.Mode
import java.time.LocalDate

@Service
@Profile(Mode.DEV)
class ReservationService(
    private val repoR: ReservationRepository
) {
     fun createReservation(reservation: Reservation): Reservation {
        val data = reservation.toEntity()
        val result = repoR.save(data)
        return result.toDomain()
    }

     fun findAllReservation() : List<Reservation> {
        return repoR.findAll().map { it.toDomain() }.toList()
    }

     fun findId(id: Long): Reservation? {
         val reservation = repoR.findById(id).orElse(null)
         return reservation?.let { it.toDomain() }
         //return repoR.findById(id).let { mapperR.toDomain(it.orElse(null)) }
    }

     fun findByProperty(property: PropertyEntity): List<Reservation>? {
        return repoR.findByProperty(property)?.map {
            it.toDomain()
        }?.toList()
    }
     fun findByUser(user: UserEntity): List<Reservation>?{
        return repoR.findByUser(user).let {list-> list?.map{it.toDomain()}?.toList() ?: emptyList() }
    }
     fun findByStatus(status: ReservationStatus): List<Reservation>{
        return repoR.findAllByStatus(status).let {list-> list?.map{ it.toDomain() }?.toList() ?: emptyList() }
    }
     fun findByMonth(month: Int, year: Int): List<Reservation>{
        return repoR.findAllByMonthAndYear(month, year).let { list-> list?.map{ it.toDomain() }?.toList() ?: emptyList() }
    }
     fun findByPYear(year : Int) : List<Reservation>{
      return repoR.findAllByYear( year)?.map{ it.toDomain() }?.toList() ?: emptyList()
    }
     fun findByDate(inputDate: LocalDate): List<Reservation> {
        return repoR.findAllByDate(inputDate).map{ it.toDomain() }.toList()
    }
     fun findByInterval(starDate : LocalDate, endDate: LocalDate): List<Reservation>? {
      return repoR.findAllInInterval(starDate, endDate)?.map { it.toDomain() }?.toList()
    }
    fun findByStartDateAndEndDateProperty(starDate : LocalDate, endDate: LocalDate, property: PropertyEntity): List<Reservation>? {
        return repoR.findByStartDateAndEndDateProperty(starDate, endDate, property)?.map { it.toDomain() }?.toList()
    }
    fun findByUserProperty(property: PropertyEntity , user: UserEntity): List<Reservation>? {
        return repoR.findByUserProperty(property, user)?.map { it.toDomain() }?.toList()
    }
        //update
        @Transactional
         fun updateStatusById(id: Long, status: ReservationStatus):Int{
          return repoR.updateStatusById(id, status)
        }
    @Transactional
     fun cancelOrKeepReservation(id: Long,isActive: Boolean, reason: String?, status: ReservationStatus):Int{
        return repoR.cancelOrKeepReservation(id, isActive, reason, status)
    }
    //delete
    @Transactional
     fun deleteReservationById(id: Long):Int{
        val success = repoR.deleteByIdReservation(id)
        return success
    }

    @Transactional
    fun deleteAllReservationByUser(user: UserEntity):Int{
        val success = repoR.deleteAllByUserReservation (user)
        return success
    }

    @Transactional
    fun deleteAllReservationByProperty(property: PropertyEntity):Int{
        val success = repoR.deleteAllByPropertyReservation(property)
        return success
    }
}