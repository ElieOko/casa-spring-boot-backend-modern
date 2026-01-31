package server.web.casa.route.favorite

import server.web.casa.route.GlobalRoute

object FavoriteScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${FavoriteFestiveFeatures.FAVORITE_FESTIVE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${FavoriteFestiveFeatures.FAVORITE_FESTIVE_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${FavoriteFestiveFeatures.FAVORITE_FESTIVE_PATH}"
}

object FavoriteFuneraireScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${FavoriteFestiveFeatures.FAVORITE_FUNERAIRE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${FavoriteFestiveFeatures.FAVORITE_FUNERAIRE_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${FavoriteFestiveFeatures.FAVORITE_FUNERAIRE_PATH}"
}

object FavoriteTerrainScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${FavoriteFestiveFeatures.FAVORITE_TERRAIN_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${FavoriteFestiveFeatures.FAVORITE_TERRAIN_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${FavoriteFestiveFeatures.FAVORITE_TERRAIN_PATH}"
}

object FavoriteHotelScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${FavoriteFestiveFeatures.ROUTE_FAVORITE_HOTEL_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${FavoriteFestiveFeatures.ROUTE_FAVORITE_HOTEL_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${FavoriteFestiveFeatures.ROUTE_FAVORITE_HOTEL_PATH}"
}

object FavoriteVacanceScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${FavoriteFestiveFeatures.FAVORITE_VACANCE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${FavoriteFestiveFeatures.FAVORITE_VACANCE_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${FavoriteFestiveFeatures.FAVORITE_VACANCE_PATH}"
}

object FavoriteBureauScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${FavoriteFestiveFeatures.FAVORITE_BUREAU_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${FavoriteFestiveFeatures.FAVORITE_BUREAU_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${FavoriteFestiveFeatures.FAVORITE_BUREAU_PATH}"
}

object FavoriteFestiveFeatures{
    const val FAVORITE_BUREAU_PATH ="bureau/favorite"
    const val FAVORITE_FESTIVE_PATH = "festive/favorite"
    const val FAVORITE_FUNERAIRE_PATH ="funeraire/favorite"
    const val ROUTE_FAVORITE_HOTEL_PATH ="hotel/favorite"
    const val FAVORITE_TERRAIN_PATH ="terrain/favorite"
    const val FAVORITE_VACANCE_PATH ="vacance/favorite"
}