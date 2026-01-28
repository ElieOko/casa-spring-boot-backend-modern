package server.web.casa.route.address

import server.web.casa.route.GlobalRoute

object AddressRoute {
    const val CITIES = "${GlobalRoute.PUBLIC}/${AddressFeatures.CITIES_PATH}"
    const val COUNTRIES = "${GlobalRoute.PUBLIC}/${AddressFeatures.COUNTRIES_PATH}"
    const val COMMUNES = "${GlobalRoute.PUBLIC}/${AddressFeatures.COMMUNES_PATH}"
    const val DISTRICTS = "${GlobalRoute.PUBLIC}/${AddressFeatures.DISTRICTS_PATH}"
    const val QUARTIERS = "${GlobalRoute.PUBLIC}/${AddressFeatures.QUARTIERS_PATH}"
}

object AddressFeatures {
    const val CITIES_PATH = "cities"
    const val COUNTRIES_PATH = "countries"
    const val COMMUNES_PATH = "communes"
    const val DISTRICTS_PATH = "districts"
    const val QUARTIERS_PATH = "quartiers"
}