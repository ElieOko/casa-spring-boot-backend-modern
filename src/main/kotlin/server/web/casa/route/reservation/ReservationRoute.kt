package server.web.casa.route.reservation

import server.web.casa.route.GlobalRoute

object ReservationRoute {
    const val RESERVATION = "${GlobalRoute.ROOT}/${ReservationFeatures.RESERVATION_PATH}"
    const val RESERVATION_WEB = "${GlobalRoute.AUTH}/${ReservationFeatures.RESERVATION_PATH}"
}

object ReservationFeatures{
    const val RESERVATION_PATH = "reservations"
}