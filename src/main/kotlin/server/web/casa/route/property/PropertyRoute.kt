package server.web.casa.route.property

import server.web.casa.route.GlobalRoute

object PropertyRoute {
    const val PROPERTY = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_PATH}"
    const val PROPERTY_FAVORITE = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_FAVORITE_PATH}"
    const val PROPERTY_FEATURE = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_FEATURE_PATH}"
    const val PROPERTY_TYPE = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_TYPE_PATH}"
    const val PROPERTY_FILTER = PropertyFeatures.PROPERTY_FILTER_PATH
    const val PROPERTY_VACANCE = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_VACANCE_PATH}"
    const val PROPERTY_BUREAU = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_BUREAUX_PATH}"
    const val PROPERTY_HOTEL = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_HOTEL_PATH}"
    const val PROPERTY_HOTEL_CHAMBRE = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_HOTEL_CHAMBRE_PATH}"
    const val PROPERTY_FESTIVE = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_FESTIVE_PATH}"
    const val PROPERTY_FUNERAIRE = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_FUNERAIRE_PATH}"
    const val PROPERTY_TERRAIN = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_TERRAIN_PATH}"
}

object PropertyFeatures{
    const val PROPERTY_PATH = "property"
    const val PROPERTY_FAVORITE_PATH = "$PROPERTY_PATH/favorites"
    const val PROPERTY_VACANCE_PATH = "$PROPERTY_PATH/vacances"
    const val PROPERTY_HOTEL_PATH = "$PROPERTY_PATH/hotels"
    const val PROPERTY_HOTEL_CHAMBRE_PATH = "$PROPERTY_PATH/hotels/chambre"
    const val PROPERTY_BUREAUX_PATH = "$PROPERTY_PATH/bureaux"
    const val PROPERTY_FESTIVE_PATH = "$PROPERTY_PATH/festives"
    const val PROPERTY_FUNERAIRE_PATH = "$PROPERTY_PATH/funeraires"
    const val PROPERTY_FEATURE_PATH = "$PROPERTY_PATH/features"
    const val PROPERTY_TERRAIN_PATH = "$PROPERTY_PATH/terrains"
    const val PROPERTY_TYPE_PATH = "$PROPERTY_PATH/types"
    const val PROPERTY_FILTER_PATH = "/filter"
}