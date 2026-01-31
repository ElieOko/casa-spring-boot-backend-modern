package server.web.casa.route.property

import server.web.casa.route.GlobalRoute

object PropertyFavoriteScope {
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_FAVORITE_PATH}"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/${PropertyFeatures.PROPERTY_FAVORITE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${PropertyFeatures.PROPERTY_FAVORITE_PATH}"
}
object PropertyFeatureScope {
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_FEATURE_PATH}"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/${PropertyFeatures.PROPERTY_FEATURE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${PropertyFeatures.PROPERTY_FEATURE_PATH}"
}
object PropertyScope{
  const val PUBLIC = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_PATH}"
  const val PRIVATE = "${GlobalRoute.PRIVATE}/${PropertyFeatures.PROPERTY_PATH}"
  const val PROTECTED = "${GlobalRoute.PROTECT}/${PropertyFeatures.PROPERTY_PATH}"
}
object PropertyVacanceScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_VACANCE_PATH}"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/${PropertyFeatures.PROPERTY_VACANCE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${PropertyFeatures.PROPERTY_VACANCE_PATH}"
}
object PropertyHotelScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_HOTEL_PATH}"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/${PropertyFeatures.PROPERTY_HOTEL_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${PropertyFeatures.PROPERTY_HOTEL_PATH}"
}
object PropertyFestiveScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_FESTIVE_PATH}"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/${PropertyFeatures.PROPERTY_FESTIVE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${PropertyFeatures.PROPERTY_FESTIVE_PATH}"
}
object PropertyFuneraireScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_FUNERAIRE_PATH}"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/${PropertyFeatures.PROPERTY_FUNERAIRE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${PropertyFeatures.PROPERTY_FUNERAIRE_PATH}"
}
object PropertyHotelChambreScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_HOTEL_CHAMBRE_PATH}"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/${PropertyFeatures.PROPERTY_HOTEL_CHAMBRE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${PropertyFeatures.PROPERTY_HOTEL_CHAMBRE_PATH}"
}
object PropertyTypeScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_TYPE_PATH}"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/${PropertyFeatures.PROPERTY_TYPE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${PropertyFeatures.PROPERTY_TYPE_PATH}"
}
object PropertyTerrainScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_TERRAIN_PATH}"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/${PropertyFeatures.PROPERTY_TERRAIN_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${PropertyFeatures.PROPERTY_TERRAIN_PATH}"
}
object PropertyBureauScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${PropertyFeatures.PROPERTY_BUREAUX_PATH}"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/${PropertyFeatures.PROPERTY_BUREAUX_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${PropertyFeatures.PROPERTY_BUREAUX_PATH}"
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
