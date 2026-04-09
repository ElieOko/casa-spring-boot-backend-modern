package server.web.casa.app.payment.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.protobuf.Message

data class ResponseTransaction(
    @JsonIgnore
    val code : String?,
    val message: String?,
    @JsonIgnore
    val orderNumber: String?
)

data class ResponseTransactionCard(
    @JsonIgnore
    val code : String?,
    val message: String?,
    @JsonIgnore
    val orderNumber: String?,
    val url : String?
)

data class TransactionCallBack(
    val code : String?,
    val reference : String?,
    val orderNumber: String?,
    val provider_reference : String?,
)
