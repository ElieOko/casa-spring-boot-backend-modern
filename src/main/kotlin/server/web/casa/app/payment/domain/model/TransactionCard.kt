package server.web.casa.app.payment.domain.model

data class TransactionCard(
    var authorization : String,
    val merchant : String,
    val reference : String,
    val amount : String,
    val currency : String,
    val description : String,
    val callback_url : String,
    val approve_url : String,
    val cancel_url : String,
    val decline_url : String
)