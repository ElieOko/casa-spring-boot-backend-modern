package server.web.casa.route.favorite

import server.web.casa.route.GlobalRoute

object FavoriteFestiveRoute {
    const val FAVORITE_PATH = "${GlobalRoute.ROOT}/${FavoriteFestiveFeatures.FAVORITE_PATH}"
    const val FAVORITE_PATH_WEB = "${GlobalRoute.AUTH}/${FavoriteFestiveFeatures.FAVORITE_PATH}"
}

object FavoriteFestiveFeatures{
    const val FAVORITE_PATH = "festive/favorite"
}