package server.web.casa.app.payment.domain.model

data class FlexCheck(
    val code : String,
    val message : String,
    val transaction : TransactionState?
)
