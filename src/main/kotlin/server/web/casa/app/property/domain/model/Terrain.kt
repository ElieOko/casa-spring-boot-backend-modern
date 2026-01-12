package server.web.casa.app.property.domain.model

import server.web.casa.app.address.domain.model.*
import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.app.property.domain.model.dto.*
import server.web.casa.app.property.infrastructure.persistence.entity.TerrainEntity
import java.time.LocalDateTime

class Terrain(
    var id: Long? = null,
    val userId: Long,
    val title: String,
    val description : String = "",
    val deviseId : Long? = null,
    val price: Double? = null,
    val surface : String? = "",
    var sold: Boolean = false,
    val address: String,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    var cityId: Long? = null,
    val postalCode: String? = "",
    var communeId: Long? = null,
    var quartierId: Long? = null,
    val transactionType: String = "",
    var propertyTypeId: Long? = null,
    var isAvailable: Boolean = true,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun Terrain.toEntity() = TerrainEntity(
    id = this.id,
    userId = this.userId,
    title = this.title,
    description = this.description,
    deviseId = this.deviseId,
    price = this.price,
    surface = this.surface,
    sold = this.sold,
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
    createdAt = createdAt,
    transactionType = this.transactionType,
    propertyTypeId = this.propertyTypeId,
)

fun Terrain.toDto() = TerrainMasterDTO(
    terrain = TerrainDTO(
        id = this.id,
        userId = this.userId,
        title = this.title,
        description = this.description,
        price = this.price,
        surface = this.surface,
        transactionType = this.transactionType,
        isAvailable = this.isAvailable,
        createdAt = this.createdAt,
    ),
    devise = Devise(id = this.deviseId!!, "", "", 0.0),
    postBy = "",
    address = AddressDTO(
        address = this.address,
        communeValue = this.communeValue,
        quartierValue = this.quartierValue,
        cityValue = this.cityValue,
        countryValue = this.countryValue,
        postalCode = this.postalCode,
    ),
    localAddress = LocalAddressDTO(
        city = City(this.cityId, 0, ""),
        commune = Commune(this.communeId, 0, emptyList(), ""),
        quartier = Quartier(this.quartierId, "")
    ),
    geoZone = GeoDTO(this.latitude, this.longitude),
    typeProperty = PropertyType(this.propertyTypeId!!, ""),
    image = ""
)