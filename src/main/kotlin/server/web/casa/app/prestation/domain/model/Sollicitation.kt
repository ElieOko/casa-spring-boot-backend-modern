package server.web.casa.app.prestation.domain.model

import server.web.casa.app.reservation.domain.model.ReservationStatus
import java.time.LocalDate

class Sollicitation(
    val id          : Long? = null,
    val userId      : Long?,
    val prestationId: Long?,
    val deviseId    : Long?,
    val budegt      : Double?,
    val description : String,
    var status      : String = ReservationStatus.PENDING.name,
    val startDate   : LocalDate,
    val endDate     : LocalDate,
    val createdAt   : LocalDate = LocalDate.now()

)