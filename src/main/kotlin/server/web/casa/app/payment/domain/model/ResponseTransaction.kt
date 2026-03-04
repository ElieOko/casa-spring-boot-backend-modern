package server.web.casa.app.payment.domain.model

import com.google.protobuf.Message

data class ResponseTransaction(
    val code : String,
    val message: String,
    val orderNumber: String
)
