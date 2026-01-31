package server.web.casa.app.prestation.domain.model

import server.web.casa.app.ecosystem.domain.model.Prestation
import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.app.prestation.infrastructure.persistance.entity.SollicitationEntity
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDate

class Sollicitation(
    val id          : Long? = null,
    val userId      : Long?,
    val prestationId: Long?,
    val deviseId    : Long?,
    val budget      : Double?,
    val description : String,
    var status      : String = ReservationStatus.PENDING.name,
    val startDate   : LocalDate,
    val endDate     : LocalDate,
    val createdAt   : LocalDate = LocalDate.now()
)

class SollicitationDTO(
    val sollicitation: SollicitationEntity,
    val user: UserDto,
    val devise: Devise,
    val prestation: Prestation,
    val userImage: String?
)