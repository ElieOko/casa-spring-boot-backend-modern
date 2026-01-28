package server.web.casa.route.sollicitation

import server.web.casa.route.GlobalRoute

object SollicitationRoute {
    const val SOLLICITATION = "${GlobalRoute.PRIVATE}/${SollicitationFeatures.SOLLICITATION_PATH}"
    const val SOLLICITATION_WEB = "${GlobalRoute.AUTH}/${SollicitationFeatures.SOLLICITATION_PATH}"
}

object SollicitationFeatures{
    const val SOLLICITATION_PATH = "sollicitation"
}