package server.web.casa.app.payment.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.*
import server.web.casa.app.payment.domain.model.*
import java.time.LocalDate

@Table(name = "paiements")
class PaiementEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("user_id")
    val userId: Long,
    @Column("reference")
    val reference : String,
    @Column("amount")
    val amount: String,
    @Column("devise")
    val devise : String = DeviseType.USD.name,
    @Column("description")
    val description : String?,
    @Column("type_payment")
    val typePayment : String = TypePayment.MOBILE_MONEY.name,
    @Column("status")
    var status : String = StatusPayment.PENDING.name,
    @Column("date_created")
    val dateCreated : LocalDate = LocalDate.now(),
    @Column("date_updated")
    val dateUpdated : LocalDate = LocalDate.now()
)

fun PaiementEntity.toDomain() = Paiement(
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