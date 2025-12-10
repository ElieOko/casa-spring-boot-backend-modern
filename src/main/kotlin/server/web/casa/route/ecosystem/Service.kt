package server.web.casa.route.ecosystem

import server.web.casa.route.GlobalRoute

object Service {
    const val DEMENAGEMENT = "${GlobalRoute.ROOT}/${ServiceFeatures.DEMENAGEMENT_PATH}"
    const val MAJORDOME = "${GlobalRoute.ROOT}/${ServiceFeatures.MAJORDOME_PATH}"
    const val AJUSTEUR = "${GlobalRoute.ROOT}/${ServiceFeatures.AJUSTEUR_PATH}"
    const val MENUSIER = "${GlobalRoute.ROOT}/${ServiceFeatures.MENUSIER_PATH}"
    const val CHAUFFEUR = "${GlobalRoute.ROOT}/${ServiceFeatures.CHAUFFEUR_PATH}"
    const val ARCHITECT = "${GlobalRoute.ROOT}/${ServiceFeatures.ARCHITECT_PATH}"
    const val CARRELEUR = "${GlobalRoute.ROOT}/${ServiceFeatures.CARRELEUR_PATH}"
    const val ELECTRICIEN = "${GlobalRoute.ROOT}/${ServiceFeatures.ELECTRICIEN_PATH}"
    const val MACON = "${GlobalRoute.ROOT}/${ServiceFeatures.MACON_PATH}"
    const val PEINTRE = "${GlobalRoute.ROOT}/${ServiceFeatures.PEINTRE_PATH}"
    const val PLOMBIER = "${GlobalRoute.ROOT}/${ServiceFeatures.PLOMBIER_PATH}"
    const val SALUBRITE = "${GlobalRoute.ROOT}/${ServiceFeatures.SALUBRITE_PATH}"
    const val FRIGORISTE = "${GlobalRoute.ROOT}/${ServiceFeatures.FRIGORISTE_PATH}"
}

object ServiceFeatures{
    const val AJUSTEUR_PATH = "ajusteurs/service"
    const val MAJORDOME_PATH = "majordomes/service"
    const val DEMENAGEMENT_PATH = "demenagements/service"
    const val ARCHITECT_PATH = "architects/service"
    const val CARRELEUR_PATH = "carreleurs/service"
    const val CHAUFFEUR_PATH = "chauffeurs/service"
    const val ELECTRICIEN_PATH = "electriciens/service"
    const val FRIGORISTE_PATH = "frigoristes/service"
    const val MACON_PATH = "macons/service"
    const val MENUSIER_PATH = "menusiers/service"
    const val PEINTRE_PATH = "peintres/service"
    const val PLOMBIER_PATH = "plombiers/service"
    const val SALUBRITE_PATH = "salubrites/service"
}