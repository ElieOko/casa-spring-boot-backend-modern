package server.web.casa.route.favorite

import server.web.casa.route.GlobalRoute

object PrestationFavoriteScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${FavoritePrestationFeatures.FAVORITE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${FavoritePrestationFeatures.FAVORITE_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${FavoritePrestationFeatures.FAVORITE_PATH}"
}

object FavoritePrestationFeatures{
    const val FAVORITE_PATH = "prestation/favorite"
}