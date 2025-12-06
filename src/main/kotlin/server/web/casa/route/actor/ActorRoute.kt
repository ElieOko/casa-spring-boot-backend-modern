package server.web.casa.route.actor

import server.web.casa.route.GlobalRoute

object ActorRoute {
    const val BAILLEUR = "${GlobalRoute.ROOT}/${ActorFeatures.BAILLEUR_PATH}"
    const val COMMISSIONNAIRE = "${GlobalRoute.ROOT}/${ActorFeatures.COMMISSIONNAIRE_PATH}"
    const val LOCATAIRE = "${GlobalRoute.ROOT}/${ActorFeatures.LOCATAIRE_PATH}"
    const val DEMENAGEMENT = "${GlobalRoute.ROOT}/${ActorFeatures.DEMENAGEMENT_PATH}"
    const val MAJORDOME = "${GlobalRoute.ROOT}/${ActorFeatures.MAJORDOME_PATH}"
    const val AJUSTEUR = "${GlobalRoute.ROOT}/${ActorFeatures.AJUSTEUR_PATH}"
    const val MENUSIER = "${GlobalRoute.ROOT}/${ActorFeatures.MENUSIER_PATH}"
    const val CHAUFFEUR = "${GlobalRoute.ROOT}/${ActorFeatures.CHAUFFEUR_PATH}"
    const val ARCHITECT = "${GlobalRoute.ROOT}/${ActorFeatures.ARCHITECT_PATH}"
    const val CARRELEUR = "${GlobalRoute.ROOT}/${ActorFeatures.CARRELEUR_PATH}"
    const val ELECTRICIEN = "${GlobalRoute.ROOT}/${ActorFeatures.ELECTRICIEN_PATH}"
    const val MACON = "${GlobalRoute.ROOT}/${ActorFeatures.MACON_PATH}"
    const val PEINTRE = "${GlobalRoute.ROOT}/${ActorFeatures.PEINTRE_PATH}"
    const val PLOMBIER = "${GlobalRoute.ROOT}/${ActorFeatures.PLOMBIER_PATH}"
    const val SALUBRITE = "${GlobalRoute.ROOT}/${ActorFeatures.SALUBRITE_PATH}"
    const val FRIGORISTE = "${GlobalRoute.ROOT}/${ActorFeatures.FRIGORISTE_PATH}"
}

object ActorFeatures{
    const val BAILLEUR_PATH = "bailleurs"
    const val COMMISSIONNAIRE_PATH = "commissionnaires"
    const val LOCATAIRE_PATH = "locataires"
    const val AJUSTEUR_PATH = "ajusteurs"
    const val MAJORDOME_PATH = "majordomes"
    const val DEMENAGEMENT_PATH = "demenagements"
    const val ARCHITECT_PATH = "architects"
    const val CARRELEUR_PATH = "carreleurs"
    const val CHAUFFEUR_PATH = "chauffeurs"
    const val ELECTRICIEN_PATH = "electriciens"
    const val FRIGORISTE_PATH = "frigoristes"
    const val MACON_PATH = "macons"
    const val MENUSIER_PATH = "menusiers"
    const val PEINTRE_PATH = "peintres"
    const val PLOMBIER_PATH = "plombiers"
    const val SALUBRITE_PATH = "salubrites"
}