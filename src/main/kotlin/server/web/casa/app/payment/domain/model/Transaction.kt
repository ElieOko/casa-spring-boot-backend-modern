package server.web.casa.app.payment.domain.model

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
    val merchant : String,
    val type : String,
    val phone : String,
    val reference : String,
    val amount : String,
    val currency : String,
    val callbackUrl : String
)