package server.web.casa.route.property

import server.web.casa.route.GlobalRoute

object PropertyRoute {
    const val PROPERTY = "${GlobalRoute.ROOT}/${PropertyFeatures.PROPERTY_PATH}"
    const val PROPERTY_FAVORITE = "${GlobalRoute.ROOT}/${PropertyFeatures.PROPERTY_FAVORITE_PATH}"
    const val PROPERTY_FEATURE = "${GlobalRoute.ROOT}/${PropertyFeatures.PROPERTY_FEATURE_PATH}"
    const val PROPERTY_TYPE = "${GlobalRoute.ROOT}/${PropertyFeatures.PROPERTY_TYPE_PATH}"
    const val PROPERTY_FILTER = PropertyFeatures.PROPERTY_FILTER_PATH
    const val PROPERTY_VACANCE = "${GlobalRoute.ROOT}/${PropertyFeatures.PROPERTY_VACANCE_PATH}"
    const val PROPERTY_BUREAU = "${GlobalRoute.ROOT}/${PropertyFeatures.PROPERTY_BUREAUX_PATH}"
    const val PROPERTY_HOTEL = "${GlobalRoute.ROOT}/${PropertyFeatures.PROPERTY_HOTEL_PATH}"
    const val PROPERTY_FESTIVE = "${GlobalRoute.ROOT}/${PropertyFeatures.PROPERTY_FESTIVE_PATH}"
    const val PROPERTY_FUNERAIRE = "${GlobalRoute.ROOT}/${PropertyFeatures.PROPERTY_FUNERAIRE_PATH}"
}

object PropertyFeatures{
    const val PROPERTY_PATH = "property"
    const val PROPERTY_FAVORITE_PATH = "$PROPERTY_PATH/favorites"
    const val PROPERTY_VACANCE_PATH = "$PROPERTY_PATH/vacances"
    const val PROPERTY_HOTEL_PATH = "$PROPERTY_PATH/hotels"
    const val PROPERTY_BUREAUX_PATH = "$PROPERTY_PATH/bureaux"
    const val PROPERTY_FESTIVE_PATH = "$PROPERTY_PATH/festives"
    const val PROPERTY_FUNERAIRE_PATH = "$PROPERTY_PATH/funeraires"
    const val PROPERTY_FEATURE_PATH = "$PROPERTY_PATH/features"
    const val PROPERTY_TYPE_PATH = "$PROPERTY_PATH/types"
    const val PROPERTY_FILTER_PATH = "/filter"
}