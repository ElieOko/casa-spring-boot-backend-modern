package server.web.casa.route.favorite

import server.web.casa.route.GlobalRoute

object FavoriteFestiveRoute {
    const val FAVORITE_PATH = "${GlobalRoute.ROOT}/${FavoriteFestiveFeatures.FAVORITE_FESTIVE_PATH}"
    const val FAVORITE_FUNERAIRE_PATH = "${GlobalRoute.ROOT}/${FavoriteFestiveFeatures.FAVORITE_FUNERAIRE_PATH}"
    const val FAVORITE_TERRAIN_PATH = "${GlobalRoute.ROOT}/${FavoriteFestiveFeatures.FAVORITE_TERRAIN_PATH}"

    //web

    const val FAVORITE_PATH_WEB = "${GlobalRoute.AUTH}/${FavoriteFestiveFeatures.FAVORITE_FESTIVE_PATH}"

}

object FavoriteFestiveFeatures{
    const val FAVORITE_FESTIVE_PATH = "festive/favorite"
    const val FAVORITE_FUNERAIRE_PATH ="funeraire/favorite"
    const val FAVORITE_TERRAIN_PATH ="terrain/favorite"
}