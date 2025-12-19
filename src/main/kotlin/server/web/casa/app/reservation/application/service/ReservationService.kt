package server.web.casa.app.reservation.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlinx.coroutines.flow.Flow
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import server.web.casa.app.reservation.infrastructure.persistence.repository.ReservationRepository
import server.web.casa.utils.Mode
import java.time.LocalDate

@Service
@Profile(Mode.DEV)
class ReservationService(
    private val repoR: ReservationRepository
) {
     suspend fun createReservation(reservation: ReservationEntity): ReservationEntity {
        val result = repoR.save(reservation)
        return result
    }

     suspend fun findAllReservation() : Flow<ReservationEntity> {
        return repoR.findAll().map { it }
    }

     suspend fun findId(id: Long): ReservationEntity? {
         val reservation = repoR.findById(id)
         return reservation
         //return repoR.findById(id).let { mapperR.toDomain(it.orElse(null)) }
    }

    suspend fun findByProperty(property: Long): List<ReservationEntity>? {
        return repoR.findByProperty(property)?.map {it}?.toList()
    }
    suspend fun findByUser(userId: Long): List<ReservationEntity>?{
        return repoR.findByUser(userId).let {list-> list?.map{it}?.toList() ?: emptyList() }
    }
    suspend fun findByStatus(status: ReservationStatus): List<ReservationEntity>{
        return repoR.findAllByStatus(status).let {list-> list?.map{ it }?.toList() ?: emptyList() }
    }
    suspend fun findByMonth(month: Int, year: Int): List<ReservationEntity>{
        return repoR.findAllByMonthAndYear(month, year).let { list-> list?.map{ it }?.toList() ?: emptyList() }
    }
    suspend fun findByPYear(year : Int) : List<ReservationEntity>{
      return repoR.findAllByYear( year)?.map{ it }?.toList() ?: emptyList()
    }
    suspend fun findByDate(inputDate: LocalDate): List<ReservationEntity> {
        return repoR.findAllByDate(inputDate).map{ it }.toList()
    }
    suspend fun findByInterval(starDate : LocalDate, endDate: LocalDate): List<ReservationEntity>? {
      return repoR.findAllInInterval(starDate, endDate)?.map { it }?.toList()
    }
    suspend fun findByStartDateAndEndDateProperty(starDate : LocalDate, endDate: LocalDate, propertyId: Long): List<ReservationEntity>? {
        return repoR.findByStartDateAndEndDateProperty(starDate, endDate, propertyId)?.map { it }?.toList()
    }
    suspend fun findByUserProperty(propertyId: Long , userId: Long): Flow<ReservationEntity>? {
        return repoR.findByUserProperty(propertyId, userId)?.map { it }
    }
        //update

    suspend fun updateStatusById(id: Long, status: ReservationStatus):Int{
          return repoR.updateStatusById(id, status)
        }

    suspend fun cancelOrKeepReservation(id: Long,isActive: Boolean, reason: String?, status: ReservationStatus):Int{
        return repoR.cancelOrKeepReservation(id, isActive, reason, status)
    }
    //delete

    suspend fun deleteReservationById(id: Long):Int{
        val success = repoR.deleteByIdReservation(id)
        return success
    }

    suspend fun deleteAllReservationByUser(user: Long):Int{
        val success = repoR.deleteAllByUserReservation (user)
        return success
    }


    suspend fun deleteAllReservationByProperty(property: Long):Int{
        val success = repoR.deleteAllByPropertyReservation(property)
        return success
    }
}