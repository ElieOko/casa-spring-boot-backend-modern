package server.web.casa.route.reservation

import server.web.casa.route.GlobalRoute

object ReservationRoute {
    const val RESERVATION = "${GlobalRoute.PRIVATE}/${ReservationFeatures.RESERVATION_PATH}"
    const val RESERVATION_BUREAU = "${GlobalRoute.PRIVATE}/${ReservationFeatures.RESERVATION_BUREAU_PATH}"
    const val RESERVATION_FESTIVE = "${GlobalRoute.PRIVATE}/${ReservationFeatures.RESERVATION_FESTIVE_PATH}"
    const val RESERVATION_FUNERAIRE = "${GlobalRoute.PRIVATE}/${ReservationFeatures.RESERVATION_FUNERAIRE_PATH}"
    const val RESERVATION_HOTEL = "${GlobalRoute.PRIVATE}/${ReservationFeatures.RESERVATION_HOTEL_PATH}"
    const val RESERVATION_TERRAIN = "${GlobalRoute.PRIVATE}/${ReservationFeatures.RESERVATION_TERRAIN_PATH}"
    const val RESERVATION_VACANCE = "${GlobalRoute.PRIVATE}/${ReservationFeatures.RESERVATION_VACANCE_PATH}"


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