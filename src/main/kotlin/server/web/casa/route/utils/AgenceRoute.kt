package server.web.casa.route.utils

import server.web.casa.route.GlobalRoute

object AgenceRoute { const val AGENCE = "${GlobalRoute.ROOT}/${AgenceFeature.AGENCE_PATH}" }
object AgenceFeature { const val AGENCE_PATH = "agences" }