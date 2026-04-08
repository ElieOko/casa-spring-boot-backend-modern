package server.web.casa.app.payment.domain.model


import jakarta.validation.constraints.Null
import org.jetbrains.annotations.*

data class TransactionState(
    val orderNumber : String,
    val reference : String,
    val amount : String,
    val amountCustomer : String,
    val currency : String,
    val createdAt : String,
    val status : String
)
data class Transaction(
    val merchant : String = "casanayo",
    val type : String = "1",
    val reference : String,
    val phone : String,
    val amount : String = "2" ,
    val currency : String = "USD",
    val callbackUrl : String = "https://migration.casanayo.com/api/v1/public/payment/mobile/callback"
)
data class TransactionRequest(
    @NotNull
    val phone : String,
    @NotNull
    val deviseId : Long,
)