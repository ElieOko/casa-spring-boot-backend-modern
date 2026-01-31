package server.web.casa.route.pub

import server.web.casa.route.GlobalRoute

object PubScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${PubFeatures.PUB_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${PubFeatures.PUB_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${PubFeatures.PUB_PATH}"
}

object PubFeatures{
    const val PUB_PATH = "pub"
}