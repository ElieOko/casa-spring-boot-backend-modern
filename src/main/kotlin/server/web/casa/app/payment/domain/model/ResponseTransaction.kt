package server.web.casa.app.payment.domain.model

import com.google.protobuf.Message

data class ResponseTransaction(
    val code : String?,
    val message: String?,
    val orderNumber: String?
)

data class TransactionCallBack(
    val code : String?,
    val reference : String?,
    val orderNumber: String?,
    val provider_reference : String?,
)
