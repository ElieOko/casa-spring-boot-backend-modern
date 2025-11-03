package server.web.casa.route.property

import server.web.casa.route.GlobalRoute

object PropertyRoute {
    const val PROPERTY = "${GlobalRoute.ROOT}/${PropertyFeatures.PROPERTY_PATH}"
    const val PROPERTY_FAVORITE = "${GlobalRoute.ROOT}/${PropertyFeatures.PROPERTY_FAVORITE_PATH}"
    const val PROPERTY_FEATURE = "${GlobalRoute.ROOT}/${PropertyFeatures.PROPERTY_FEATURE_PATH}\""
    const val PROPERTY_TYPE = "${GlobalRoute.ROOT}/${PropertyFeatures.PROPERTY_TYPE_PATH}\""
    const val PROPERTY_FILTER = "${GlobalRoute.ROOT}/${PropertyFeatures.PROPERTY_FILTER_PATH}\""
}

object PropertyFeatures{
    const val PROPERTY_PATH = "property"
    const val PROPERTY_FAVORITE_PATH = "$PROPERTY_PATH/favorites"
    const val PROPERTY_FEATURE_PATH = "$PROPERTY_PATH/features"
    const val PROPERTY_TYPE_PATH = "$PROPERTY_PATH/types"
    const val PROPERTY_FILTER_PATH = "$PROPERTY_PATH/filter"
}