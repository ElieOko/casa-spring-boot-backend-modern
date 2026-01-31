package server.web.casa.route.ecosystem

import server.web.casa.route.GlobalRoute

object PrestationScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${ServiceFeatures.PRESTATION_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${ServiceFeatures.PRESTATION_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${ServiceFeatures.PRESTATION_PATH}"
}
object ServiceFeatures{
    const val PRESTATION_PATH = "prestations"
}