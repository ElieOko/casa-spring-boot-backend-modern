package server.web.casa.route.favorite

import server.web.casa.route.GlobalRoute

object FavoriteFestiveRoute {
    //api

    const val FAVORITE_PATH = "${GlobalRoute.ROOT}/${FavoriteFestiveFeatures.FAVORITE_FESTIVE_PATH}"
    const val FAVORITE_FUNERAIRE_PATH = "${GlobalRoute.ROOT}/${FavoriteFestiveFeatures.FAVORITE_FUNERAIRE_PATH}"
    const val FAVORITE_TERRAIN_PATH = "${GlobalRoute.ROOT}/${FavoriteFestiveFeatures.FAVORITE_TERRAIN_PATH}"
    const val ROUTE_FAVORITE_HOTEL ="${GlobalRoute.ROOT}/${FavoriteFestiveFeatures.ROUTE_FAVORITE_HOTEL_PATH}"
    const val FAVORITE_VACANCE_PATH = "${GlobalRoute.ROOT}/${FavoriteFestiveFeatures.FAVORITE_VACANCE_PATH}"

    //web

    const val FAVORITE_PATH_WEB = "${GlobalRoute.AUTH}/${FavoriteFestiveFeatures.FAVORITE_FESTIVE_PATH}"

}

object FavoriteFestiveFeatures{
    const val FAVORITE_FESTIVE_PATH = "festive/favorite"
    const val FAVORITE_FUNERAIRE_PATH ="funeraire/favorite"
    const val ROUTE_FAVORITE_HOTEL_PATH ="hotel/favorite"
    const val FAVORITE_TERRAIN_PATH ="terrain/favorite"
    const val FAVORITE_VACANCE_PATH ="vacance/favorite"
}