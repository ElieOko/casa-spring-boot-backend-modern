package server.web.casa.app.reservation.application.command

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyRepository
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.domain.model.ReservationType
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import server.web.casa.app.reservation.infrastructure.persistence.repository.ReservationRepository
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import server.web.casa.utils.Mode
import java.time.LocalDate

@Component
@Order(7)
@Profile(Mode.DEV)
class ReservatonCommand(
    private val property: PropertyRepository,
    private val user: UserRepository,
    private val reservation: ReservationRepository
): CommandLineRunner {
    private val log = LoggerFactory.getLogger(this::class.java)
    override fun run(vararg args: String) {
        try {
            log.info("***start command reservation***")
//            createReserv()
        }
        catch (e : Exception){
            log.info("***error command reservation: "+e.message+" cause: "+e.cause+" tlocalize :"+e.localizedMessage)
        }
    }

//   suspend fun createReserv(){
//       var prop : PropertyEntity? = null
//       property.findById(1)?.let{
//        prop = it
//       }
//       var u : UserEntity? = null
//         user.findById(1)?.let{
//           u = it
//       }
//       if (u != null && prop!= null){
//           val start: LocalDate = LocalDate.of(2025, 10, 22)
//           val end: LocalDate = LocalDate.of(2025, 11, 10)
//           val data = ReservationEntity(
//               property = prop,
//               user = u,
//               message = "Your reservation",
//               reservationHeure = "12:00:00",
//               isActive = true,
//               startDate = start,
//               endDate = end,
//               createdAt = LocalDate.now(),
//               status = ReservationStatus.PENDING,
//               type = ReservationType.STANDARD,
//               cancellationReason = "No"
//           )
//           val create = reservation.save(data)
//           log.info("***La melo est gangs success, you got it?***")
//       }
//
//   }
}