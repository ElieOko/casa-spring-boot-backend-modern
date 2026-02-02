package server.web.casa.app.reservation.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import server.web.casa.app.property.application.service.BureauService
import server.web.casa.app.property.infrastructure.persistence.repository.BureauImageRepository
import server.web.casa.app.reservation.domain.model.ReservationBureauDTO
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationBureauEntity
import server.web.casa.app.reservation.infrastructure.persistence.repository.ReservationBureauRepository
import server.web.casa.app.user.application.service.UserService
import server.web.casa.utils.Mode
import java.time.LocalDate

@Service
@Profile(Mode.DEV)
class ReservationBureauService(
    private val repoR: ReservationBureauRepository,
    private val brxS: BureauService,
    private val userS: UserService
) {
     suspend fun createReservation(reservation: ReservationBureauEntity): ReservationBureauDTO {
        val result = repoR.save(reservation)
        return toEntityDTO(result)
    }

     suspend fun findAllReservation() : List<ReservationBureauDTO> {

         return repoR.findAll().map{toEntityDTO(it)}.toList()
    }

     suspend fun findById(id: Long): ReservationBureauDTO? {
         val reservation = repoR.findById(id) ?: return null
         return toEntityDTO(reservation)
         //return repoR.findById(id).let { mapperR.toDomain(it.orElse(null)) }
    }
    suspend fun findByHostUser(userId: Long):List<ReservationBureauDTO>?{
        return repoR.findByHostUserId(userId).map {
            toEntityDTO(it)
        }.toList()

    }
    suspend fun findByProperty(bureauId: Long): List<ReservationBureauDTO>? {
        return repoR.findByProperty(bureauId)?.map {
                toEntityDTO(it)
            }?.toList()
    }
    suspend fun findByUser(userId: Long): List<ReservationBureauDTO>?{
        return repoR.findByUser(userId).let {list-> list?.map{
                toEntityDTO(it)
            }?.toList() ?: emptyList() }
    }
    suspend fun findByStatus(status: ReservationStatus): List<ReservationBureauDTO>{
        return repoR.findAllByStatus(status).let {list-> list?.map{
            toEntityDTO(it)
        }?.toList() ?: emptyList() }
    }
    suspend fun findByMonth(month: Int, year: Int): List<ReservationBureauDTO>{
        return repoR.findAllByMonthAndYear(month, year).let { list-> list?.map{
            toEntityDTO(it)
        }?.toList() ?: emptyList() }
    }
    suspend fun findByPYear(year : Int) : List<ReservationBureauDTO>{
      return repoR.findAllByYear( year)?.map{
          toEntityDTO(it)
      }?.toList() ?: emptyList()
    }
    suspend fun findByDate(inputDate: LocalDate): List<ReservationBureauDTO> {
        return repoR.findAllByDate(inputDate).map{
            toEntityDTO(it)
        }.toList()
    }
    suspend fun findByInterval(starDate : LocalDate, endDate: LocalDate): List<ReservationBureauDTO>? {
      return repoR.findAllInInterval(starDate, endDate)?.map {
          toEntityDTO(it)
      }?.toList()
    }
    suspend fun findByStartDateAndEndDateProperty(starDate : LocalDate, endDate: LocalDate, propertyId: Long): List<ReservationBureauDTO>? {
        return repoR.findByStartDateAndEndDateProperty(starDate, endDate, propertyId)?.map {
                toEntityDTO(it)
             }?.toList()
    }
    suspend fun findByUserProperty(propertyId: Long , userId: Long): List<ReservationBureauDTO>? {
        return repoR.findByUserProperty(propertyId, userId)?.map {
            toEntityDTO(it)
        }?.toList()
    }

    suspend fun updateStatusById(id: Long, status: ReservationStatus): ReservationBureauDTO {
            val entity = repoR.findById(id)
                entity?.status = status.toString()
                repoR.save(entity!!)
            val reservation = repoR.findById(id)!!
            return toEntityDTO(reservation)
        }

    suspend fun cancelOrKeepReservation(id: Long,isActive: Boolean, reason: String?, status: ReservationStatus): ReservationBureauDTO {
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

    suspend fun toEntityDTO(it: ReservationBureauEntity): ReservationBureauDTO =
        ReservationBureauDTO(
            reservation = it,
            bureau = brxS.findById(it.bureauId!!),
            user = userS.findIdUser(it.userId!!),
            bureauImage = brxS.getImageByBureauID(it.bureauId).toList()
           // propietaire = userS.findIdUser(brxS.findById(it.bureauId).userId!!)
        )

}