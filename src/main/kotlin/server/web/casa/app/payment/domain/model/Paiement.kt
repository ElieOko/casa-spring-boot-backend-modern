package server.web.casa.app.payment.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import server.web.casa.app.payment.infrastructure.persistence.entity.PaiementEntity
import server.web.casa.app.user.domain.model.*
import java.time.*

data class Paiement(
    val id: Long? = null,
    val userId: Long,
    val reference : String,
    val amount: String,
    val devise : String = DeviseType.USD.name,
    val description : String?,
    val typePayment : String = TypePayment.MOBILE_MONEY.name,
    var status : String = StatusPayment.PENDING.name,
    val dateCreated : LocalDate = LocalDate.now(),
    val dateUpdated : LocalDate = LocalDate.now()
)
fun Paiement.toEntity() = PaiementEntity(
    id = this.id,
    userId = this.userId,
    reference = this.reference,
    amount = this.amount,
    devise = this.devise,
    description = this.description,
    typePayment = this.typePayment,
    status = this.status,
    dateCreated = this.dateCreated,
    dateUpdated = this.dateUpdated
)
data class PaymentDTO(
    val payment : List<Paiement>,
    val user : UserDto
)

enum class TypePayment { MOBILE_MONEY,CARD }
enum class DeviseType { USD,CDF }