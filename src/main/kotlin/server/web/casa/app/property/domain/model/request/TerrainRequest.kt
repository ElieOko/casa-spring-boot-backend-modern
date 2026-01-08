package server.web.casa.app.property.domain.model.request

import jakarta.validation.constraints.NotNull
import server.web.casa.app.ecosystem.domain.request.ImageRequest
import server.web.casa.app.property.domain.model.Terrain

data class TerrainRequest(
    @NotNull
    val userId: Long,
    @NotNull
    val title: String,
    val description : String = "",
    val deviseId : Long? = null,
    @NotNull
    val price: Double? = null,
    val surface : String? = "",
    @NotNull
    val address: String,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    var cityId: Long? = null,
    val postalCode: String? = "",
    var communeId: Long? = null,
    var quartierId: Long? = null,
    @NotNull
    val transactionType: String = "",
    @NotNull
    val propertyTypeId: Long? = null,
    var isAvailable: Boolean = true,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @NotNull
    val images : List<ImageRequest>,
)
fun TerrainRequest.toDomain() = Terrain(
    id = null,
    userId = this.userId,
    title = this.title,
    description = this.description,
    deviseId = this.deviseId,
    price = this.price,
    surface = this.surface,
    address = this.address,
    communeId = this.communeId,
    quartierId = this.quartierId,
    cityId = this.cityId,
    countryValue = this.countryValue,
    cityValue = this.cityValue,
    postalCode = this.postalCode,
    communeValue = this.communeValue,
    isAvailable = this.isAvailable,
    latitude = latitude,
    longitude = longitude,
    transactionType = this.transactionType,
    propertyTypeId = this.propertyTypeId,
)
