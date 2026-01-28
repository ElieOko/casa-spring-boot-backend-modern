package server.web.casa.route.sollicitation

import server.web.casa.route.GlobalRoute

object CotationScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${CotationFeatures.COTATION_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${CotationFeatures.COTATION_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${CotationFeatures.COTATION_PATH}"
}

object CotationFeatures{
    const val COTATION_PATH = "cotation"
}