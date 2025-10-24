package server.web.casa.app.reservation.application.service

import jakarta.transaction.Transactional
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyRepository
import server.web.casa.app.reservation.domain.model.Reservation
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.mapper.ReservationMapper
import server.web.casa.app.reservation.infrastructure.persistence.repository.ReservationRepository
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import server.web.casa.utils.Mode
import java.time.LocalDate

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
            .orElseThrow { RuntimeException("Reservation with id $id not found, try again with the true value") }
        return mapperR.toDomain(entity)
    }

    fun findByProperty(property: PropertyEntity): List<Reservation> {
        val listEntity = repoR.findByProperty(property).orEmpty()
        return listEntity.map { mapperR.toDomain(it) }
    }
    fun findByUser(user: UserEntity): List<Reservation>{
        val listEntity = repoR.findByUser(user).orEmpty()
        return listEntity.map{mapperR.toDomain(it)}
    }
    fun findByStatus(status: ReservationStatus): List<Reservation>{
        val listEntity = repoR.findAllByStatus(status).orEmpty()
        return  listEntity.map{ mapperR.toDomain(it) }
    }
    fun findByMonth(month: Int, year: Int): List<Reservation>{
        val listEntity = repoR.findAllByMonthAndYear(month, year).orEmpty()
        return  listEntity.map{ mapperR.toDomain(it) }
    }
    fun findByPYear(year : Int) : List<Reservation>{
        val listEntity = repoR.findAllByYear( year).orEmpty()
        return  listEntity.map{ mapperR.toDomain(it) }
    }
    fun findByDate(inputDate: LocalDate): List<Reservation>{
        val listEntity = repoR.findAllByDate(inputDate).orEmpty()
        return  listEntity.map{ mapperR.toDomain(it) }
    }
    fun findByInterval(starDate : LocalDate, endDate: LocalDate):List<Reservation> {
        val listEntity = repoR.findAllInInterval(starDate, endDate).orEmpty()
        return listEntity.map { mapperR.toDomain(it) }
        }
        //update
        @Transactional
        fun updateStatusById(id: Long, status: ReservationStatus):Int{
            val success = repoR.updateStatusById(id, status)
            return success
        }

    //delete
    @Transactional
    fun deleteReservationById(id: Long):Int{
        val success = repoR.deleteByIdReservation(id)
        return success
    }
}