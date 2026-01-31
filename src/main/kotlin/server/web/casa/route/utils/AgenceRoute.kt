package server.web.casa.route.utils

import server.web.casa.route.GlobalRoute

object AgenceScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${AgenceFeature.AGENCE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${AgenceFeature.AGENCE_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${AgenceFeature.AGENCE_PATH}"
}
object CardTypeScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${AgenceFeature.CARD_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${AgenceFeature.CARD_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${AgenceFeature.CARD_PATH}"
}
object NotificationScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/notifications"
    const val PROTECTED = "${GlobalRoute.PROTECT}/notifications"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/notifications"
}
object AgenceFeature {
    const val AGENCE_PATH = "agences"
    const val CARD_PATH = "cards"
    const val DEVISE = "devises"
}