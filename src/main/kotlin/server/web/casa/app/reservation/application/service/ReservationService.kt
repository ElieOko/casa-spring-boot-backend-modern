package server.web.casa.app.reservation.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlinx.coroutines.flow.Flow
import server.web.casa.app.property.application.service.PropertyService
import server.web.casa.app.reservation.domain.model.ReservationDTO
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import server.web.casa.app.reservation.infrastructure.persistence.repository.ReservationRepository
import server.web.casa.utils.Mode
import java.time.LocalDate

@Service
@Profile(Mode.DEV)
class ReservationService(
    private val repoR: ReservationRepository,
    private val propS: PropertyService
) {
     suspend fun createReservation(reservation: ReservationEntity): ReservationDTO {
        val result = repoR.save(reservation)
        return toEntityDTO(result)
    }

     suspend fun findAllReservation() : List<ReservationDTO> {

         return repoR.findAll().map{toEntityDTO(it)}.toList()
    }

     suspend fun findById(id: Long): ReservationDTO? {
         val reservation = repoR.findById(id) ?: return null
         return toEntityDTO(reservation)
         //return repoR.findById(id).let { mapperR.toDomain(it.orElse(null)) }
    }

    suspend fun findByProperty(property: Long): List<ReservationDTO>? {
        return repoR.findByProperty(property)?.map {
                toEntityDTO(it)
            }?.toList()
    }
    suspend fun findByUser(userId: Long): List<ReservationDTO>?{
        return repoR.findByUser(userId).let {list-> list?.map{
                toEntityDTO(it)
            }?.toList() ?: emptyList() }
    }
    suspend fun findByStatus(status: ReservationStatus): List<ReservationDTO>{
        return repoR.findAllByStatus(status).let {list-> list?.map{
            toEntityDTO(it)
        }?.toList() ?: emptyList() }
    }
    suspend fun findByMonth(month: Int, year: Int): List<ReservationDTO>{
        return repoR.findAllByMonthAndYear(month, year).let { list-> list?.map{
            toEntityDTO(it)
        }?.toList() ?: emptyList() }
    }
    suspend fun findByPYear(year : Int) : List<ReservationDTO>{
      return repoR.findAllByYear( year)?.map{
          toEntityDTO(it)
      }?.toList() ?: emptyList()
    }
    suspend fun findByDate(inputDate: LocalDate): List<ReservationDTO> {
        return repoR.findAllByDate(inputDate).map{
            toEntityDTO(it)
        }.toList()
    }
    suspend fun findByInterval(starDate : LocalDate, endDate: LocalDate): List<ReservationDTO>? {
      return repoR.findAllInInterval(starDate, endDate)?.map {
          toEntityDTO(it)
      }?.toList()
    }
    suspend fun findByStartDateAndEndDateProperty(starDate : LocalDate, endDate: LocalDate, propertyId: Long): List<ReservationDTO>? {
        return repoR.findByStartDateAndEndDateProperty(starDate, endDate, propertyId)?.map {
                toEntityDTO(it)
             }?.toList()
    }
    suspend fun findByUserProperty(propertyId: Long , userId: Long): List<ReservationDTO>? {
        return repoR.findByUserProperty(propertyId, userId)?.map {
            toEntityDTO(it)
        }?.toList()
    }

    suspend fun updateStatusById(id: Long, status: ReservationStatus):ReservationDTO {
            val entity = repoR.findById(id)
                entity?.status = status.toString()
                repoR.save(entity!!)
            val reservation = repoR.findById(id)!!
            return toEntityDTO(reservation)
        }

    suspend fun cancelOrKeepReservation(id: Long,isActive: Boolean, reason: String?, status: ReservationStatus): ReservationDTO {
        val entity = repoR.findById(id)
            entity?.status = status.toString()
            entity?.isActive = isActive
            entity?.cancellationReason = reason
            repoR.save(entity!!)
        val reservation = repoR.findById(id)!!
        return toEntityDTO(reservation)
    }
    //delete

    suspend fun deleteReservationById(id: Long): Boolean{
        val success= repoR.deleteById(id)
        return true
    }
    suspend fun deleteAll(): Boolean{
        val success= repoR.deleteAll()
        return true
    }


    suspend fun deleteAllReservationByUser(user: Long):Mono<Int>{
        val success = repoR.deleteAllByUserReservation (user)
        return success
    }

    suspend fun deleteAllReservationByProperty(property: Long):Mono<Int>{
        val success = repoR.deleteAllByPropertyReservation(property)
        return success
    }

    suspend fun toEntityDTO(it: ReservationEntity): ReservationDTO =
        ReservationDTO(
            reservation = it,
            property = propS.findByIdProperty(it.propertyId!!).first
        )

}