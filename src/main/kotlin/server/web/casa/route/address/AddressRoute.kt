package server.web.casa.route.address

import server.web.casa.route.GlobalRoute

object CityScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${AddressFeatures.CITIES_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${AddressFeatures.CITIES_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${AddressFeatures.CITIES_PATH}"
}
object CountryScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${AddressFeatures.COUNTRIES_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${AddressFeatures.COUNTRIES_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${AddressFeatures.COUNTRIES_PATH}"
}
object CommuneScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${AddressFeatures.COMMUNES_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${AddressFeatures.COMMUNES_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${AddressFeatures.COMMUNES_PATH}"
}
object DistrictScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${AddressFeatures.DISTRICTS_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${AddressFeatures.DISTRICTS_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${AddressFeatures.DISTRICTS_PATH}"
}
object QuartierScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${AddressFeatures.QUARTIERS_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${AddressFeatures.QUARTIERS_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${AddressFeatures.QUARTIERS_PATH}"
}

object AddressFeatures {
    const val CITIES_PATH = "cities"
    const val COUNTRIES_PATH = "countries"
    const val COMMUNES_PATH = "communes"
    const val DISTRICTS_PATH = "districts"
    const val QUARTIERS_PATH = "quartiers"
}