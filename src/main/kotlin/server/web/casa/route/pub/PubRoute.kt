package server.web.casa.route.pub

import server.web.casa.route.GlobalRoute

object PubRoute {
    const val PUB_PATH = "${GlobalRoute.PROTECT}/${PubFeatures.PUB_PATH}"
    const val PUB_PATH_WEB = "${GlobalRoute.AUTH}/${PubFeatures.PUB_PATH}"
}

object PubFeatures{
    const val PUB_PATH = "pub"
}