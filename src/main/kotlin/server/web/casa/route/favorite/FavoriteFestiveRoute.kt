package server.web.casa.route.favorite

import server.web.casa.route.GlobalRoute

object FavoriteFestiveRoute {
    //api

    const val FAVORITE_PATH = "${GlobalRoute.PROTECT}/${FavoriteFestiveFeatures.FAVORITE_FESTIVE_PATH}"
    const val FAVORITE_FUNERAIRE_PATH = "${GlobalRoute.PROTECT}/${FavoriteFestiveFeatures.FAVORITE_FUNERAIRE_PATH}"
    const val FAVORITE_TERRAIN_PATH = "${GlobalRoute.PROTECT}/${FavoriteFestiveFeatures.FAVORITE_TERRAIN_PATH}"
    const val ROUTE_FAVORITE_HOTEL ="${GlobalRoute.PROTECT}/${FavoriteFestiveFeatures.ROUTE_FAVORITE_HOTEL_PATH}"
    const val FAVORITE_VACANCE_PATH = "${GlobalRoute.PROTECT}/${FavoriteFestiveFeatures.FAVORITE_VACANCE_PATH}"
    const val FAVORITE_BUREAU = "${GlobalRoute.PROTECT}/${FavoriteFestiveFeatures.FAVORITE_BUREAU_PATH}"

    //web

    const val FAVORITE_PATH_WEB = "${GlobalRoute.AUTH}/${FavoriteFestiveFeatures.FAVORITE_FESTIVE_PATH}"

}

object FavoriteFestiveFeatures{
    const val FAVORITE_BUREAU_PATH ="bureau/favorite"
    const val FAVORITE_FESTIVE_PATH = "festive/favorite"
    const val FAVORITE_FUNERAIRE_PATH ="funeraire/favorite"
    const val ROUTE_FAVORITE_HOTEL_PATH ="hotel/favorite"
    const val FAVORITE_TERRAIN_PATH ="terrain/favorite"
    const val FAVORITE_VACANCE_PATH ="vacance/favorite"
}