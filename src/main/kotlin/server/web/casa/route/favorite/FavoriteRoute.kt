package server.web.casa.route.favorite

import server.web.casa.route.GlobalRoute

object PropertyFavoriteScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${FavoriteFeatures.FAVORITE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${FavoriteFeatures.FAVORITE_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${FavoriteFeatures.FAVORITE_PATH}"
}

object FavoriteFeatures{
    const val FAVORITE_PATH = "favorite"
}