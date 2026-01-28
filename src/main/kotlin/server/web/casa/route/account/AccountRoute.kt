package server.web.casa.route.account

import server.web.casa.route.GlobalRoute

object AccountRoute {
    const val ACCOUNT = "${GlobalRoute.PUBLIC}/${AccountFeatures.ACCOUNT_PATH}"
    const val ACCOUNT_TYPE = "${GlobalRoute.PUBLIC}/${AccountFeatures.ACCOUNT_TYPE_PATH}"
}

object AccountFeatures {
    const val ACCOUNT_PATH = "accounts"
    const val ACCOUNT_TYPE_PATH = "accounts/type"
}