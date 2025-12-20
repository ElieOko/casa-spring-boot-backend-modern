package server.web.casa.route.ecosystem

import server.web.casa.route.GlobalRoute

object Service {
    const val PRESTATION = "${GlobalRoute.ROOT}/${ServiceFeatures.PRESTATION_PATH}"
}

object ServiceFeatures{
    const val PRESTATION_PATH = "prestations"

}