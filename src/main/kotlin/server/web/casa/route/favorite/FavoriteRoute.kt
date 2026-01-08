package server.web.casa.route.favorite

import server.web.casa.route.GlobalRoute

object FavoriteRoute {
    const val FAVORITE_PATH = "${GlobalRoute.ROOT}/${FavoriteFeatures.FAVORITE_PATH}"
    const val FAVORITE_PATH_WEB = "${GlobalRoute.AUTH}/${FavoriteFeatures.FAVORITE_PATH}"
}

object FavoriteFeatures{
    const val FAVORITE_PATH = "favorite"
}