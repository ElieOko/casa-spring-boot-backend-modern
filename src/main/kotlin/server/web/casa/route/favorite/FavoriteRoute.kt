package server.web.casa.route.favorite

import server.web.casa.route.GlobalRoute

object FavoriteRoute {
    const val FAVORITE_PATH = "${GlobalRoute.ROOT}/${ReservationFeatures.FAVORITE_PATH}"
    const val FAVORITE_PATH_WEB = "${GlobalRoute.AUTH}/${ReservationFeatures.FAVORITE_PATH}"
}

object ReservationFeatures{
    const val FAVORITE_PATH = "favorite"
}