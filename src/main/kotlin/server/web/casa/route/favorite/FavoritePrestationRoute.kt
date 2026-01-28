package server.web.casa.route.favorite

import server.web.casa.route.GlobalRoute

object FavoritePrestationRoute {
    const val FAVORITE_PATH = "${GlobalRoute.PROTECT}/${FavoritePrestationFeatures.FAVORITE_PATH}"
    const val FAVORITE_PATH_WEB = "${GlobalRoute.AUTH}/${FavoritePrestationFeatures.FAVORITE_PATH}"
}

object FavoritePrestationFeatures{
    const val FAVORITE_PATH = "prestation/favorite"
}