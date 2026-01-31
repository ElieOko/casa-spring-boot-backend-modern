package server.web.casa.route.sollicitation

import server.web.casa.route.GlobalRoute

object SollicitationScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${SollicitationFeatures.SOLLICITATION_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${SollicitationFeatures.SOLLICITATION_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${SollicitationFeatures.SOLLICITATION_PATH}"
}

object SollicitationFeatures{
    const val SOLLICITATION_PATH = "sollicitation"
}