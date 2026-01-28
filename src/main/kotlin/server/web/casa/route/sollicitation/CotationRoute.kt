package server.web.casa.route.sollicitation

import server.web.casa.route.GlobalRoute

object CotationRoute {
    const val COTATION = "${GlobalRoute.PRIVATE}/${CotationFeatures.COTATION_PATH}"
    const val COTATION_WEB = "${GlobalRoute.AUTH}/${CotationFeatures.COTATION_PATH}"
}

object CotationFeatures{
    const val COTATION_PATH = "cotation"
}