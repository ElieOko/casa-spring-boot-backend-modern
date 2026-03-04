package server.web.casa.app.payment.domain.model

import server.web.casa.app.reservation.domain.model.ReservationStatus
import java.time.LocalDate

data class Paiement(
    val id: Long? = null,
    val userId: Long,
    val reference : String,
    val amount: String,
    val devise : String = DeviseType.USD.name,
    val description : String?,
    val typePayment : String = TypePayment.MOBILE_MONEY.name,
    val status : String = ReservationStatus.PENDING.name,
    val dateCreated : LocalDate = LocalDate.now(),
    val dateUpdated : LocalDate = LocalDate.now()
)

enum class TypePayment { MOBILE_MONEY,CARD }
enum class DeviseType { USD,CDF }