package server.web.casa.route.utils

import server.web.casa.route.GlobalRoute

object AgenceRoute {
    const val AGENCE = "${GlobalRoute.ROOT}/${AgenceFeature.AGENCE_PATH}"
    const val CARD_TYPE = "${GlobalRoute.ROOT}/${AgenceFeature.CARD_PATH}"
    const val DEVISE = "${GlobalRoute.ROOT}/${AgenceFeature.DEVISE}"
    const val NOTIFICATION = "${GlobalRoute.ROOT}/notifications"
}
object AgenceFeature {
    const val AGENCE_PATH = "agences"
    const val CARD_PATH = "cards"
    const val DEVISE = "devises"
}