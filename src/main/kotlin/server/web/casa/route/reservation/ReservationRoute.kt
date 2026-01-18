package server.web.casa.route.reservation

import server.web.casa.route.GlobalRoute

object ReservationRoute {
    const val RESERVATION = "${GlobalRoute.ROOT}/${ReservationFeatures.RESERVATION_PATH}"
    const val RESERVATION_BUREAU = "${GlobalRoute.ROOT}/${ReservationFeatures.RESERVATION_BUREAU_PATH}"
    const val RESERVATION_FESTIVE = "${GlobalRoute.ROOT}/${ReservationFeatures.RESERVATION_FESTIVE_PATH}"
    const val RESERVATION_FUNERAIRE = "${GlobalRoute.ROOT}/${ReservationFeatures.RESERVATION_FUNERAIRE_PATH}"
    const val RESERVATION_HOTEL = "${GlobalRoute.ROOT}/${ReservationFeatures.RESERVATION_HOTEL_PATH}"
    const val RESERVATION_TERRAIN = "${GlobalRoute.ROOT}/${ReservationFeatures.RESERVATION_TERRAIN_PATH}"
    const val RESERVATION_VACANCE = "${GlobalRoute.ROOT}/${ReservationFeatures.RESERVATION_VACANCE_PATH}"


    const val RESERVATION_WEB = "${GlobalRoute.AUTH}/${ReservationFeatures.RESERVATION_PATH}"
}

object ReservationFeatures{
    const val RESERVATION_PATH = "reservations"
    const val RESERVATION_BUREAU_PATH = "reservations/bureau"
    const val RESERVATION_FESTIVE_PATH = "reservations/festive"
    const val RESERVATION_FUNERAIRE_PATH = "reservations/funeraire"
    const val RESERVATION_HOTEL_PATH = "reservations/chambre"
    const val RESERVATION_TERRAIN_PATH = "reservations/terrain"
    const val RESERVATION_VACANCE_PATH = "reservations/vacance"
}