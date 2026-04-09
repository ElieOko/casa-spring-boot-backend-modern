package server.web.casa.app.payment.domain.model

import org.jetbrains.annotations.NotNull

data class TransactionCard(
    var authorization : String="",
    val merchant : String = "casanayo",
    val reference : String,
    val amount : String,
    val currency : String = "USD",
    val description : String = "Abonnement sur la plateforme",
    val callback_url : String = "https://migration.casanayo.com/api/v1/public/payment/card/callback",
    val approve_url : String = "https://migration.casanayo.com/api/v1/public/payment/card/callback/approve",
    val cancel_url : String = "https://migration.casanayo.com/api/v1/public/payment/card/callback/cancel",
    val decline_url : String = "https://migration.casanayo.com/api/v1/public/payment/card/callback/decline"
)
data class TransactionCardRequest(
    @NotNull
    val deviseId : Long,
)