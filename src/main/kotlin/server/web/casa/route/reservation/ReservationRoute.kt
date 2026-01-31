package server.web.casa.route.reservation

import server.web.casa.route.GlobalRoute

object ReservationScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${ReservationFeatures.RESERVATION_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${ReservationFeatures.RESERVATION_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${ReservationFeatures.RESERVATION_PATH}"
}

object ReservationBureauScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${ReservationFeatures.RESERVATION_BUREAU_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${ReservationFeatures.RESERVATION_BUREAU_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${ReservationFeatures.RESERVATION_BUREAU_PATH}"
}

object ReservationFestiveScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${ReservationFeatures.RESERVATION_FESTIVE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${ReservationFeatures.RESERVATION_FESTIVE_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${ReservationFeatures.RESERVATION_FESTIVE_PATH}"
}

object ReservationFuneraireScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${ReservationFeatures.RESERVATION_FUNERAIRE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${ReservationFeatures.RESERVATION_FUNERAIRE_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${ReservationFeatures.RESERVATION_FUNERAIRE_PATH}"
}

object ReservationHotelScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${ReservationFeatures.RESERVATION_HOTEL_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${ReservationFeatures.RESERVATION_HOTEL_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${ReservationFeatures.RESERVATION_HOTEL_PATH}"
}

object ReservationTerrainScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${ReservationFeatures.RESERVATION_TERRAIN_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${ReservationFeatures.RESERVATION_TERRAIN_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${ReservationFeatures.RESERVATION_TERRAIN_PATH}"
}

object ReservationVacanceScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${ReservationFeatures.RESERVATION_VACANCE_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${ReservationFeatures.RESERVATION_VACANCE_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${ReservationFeatures.RESERVATION_VACANCE_PATH}"
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