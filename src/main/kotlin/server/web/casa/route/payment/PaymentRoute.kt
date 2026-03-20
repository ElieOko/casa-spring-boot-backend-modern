package server.web.casa.route.payment

import server.web.casa.route.GlobalRoute

object PaymentScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${PaymentFeatures.PAYMENT_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${PaymentFeatures.PAYMENT_PATH}"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/${PaymentFeatures.PAYMENT_PATH}"
}

object PaymentFeatures {
    const val PAYMENT_PATH = "payment"
}