package server.web.casa.app.ecosystem.domain.request

data class SalubriteServiceRequest(
    val service : TaskRequest,
    val realisation : List<ImageRequest>
)
data class ImageRequest(
    val image : String = ""
)
data class TaskRequest(
    val userId : Long,
    val deviseId : Long,
    val experience : String,
    val description : String?="",
    val address : String?="",
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    val minPrice: Double = 0.0,
    val maxPrice: Double = 0.0,
    val cityId : Long,
    val communeId : Long,
    val quartierId : Long,
)