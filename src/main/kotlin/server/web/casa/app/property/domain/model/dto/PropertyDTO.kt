package server.web.casa.app.property.domain.model.dto

import server.web.casa.app.property.domain.model.AddressDTO
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity

data class PropertyDTO(
    val propertyId: Long = 0,
    var title: String,
    var description: String? = "",
    var price: Double,
    var surface: Double? = null,
    var rooms: Int? = 0,
    val bedrooms: Int? = 0,
    var electric: Int? = 1,
    var water: Int? = 1,
    var kitchen: Int? = 0,
    val livingRoom: Int? = 0,
    var guarantee: String = "",
    var bathroom: Int? = 0,
    var floor: Int? = 0,
    var sold: Boolean,
    var transactionType: String,
    val isAvailable: Boolean = true,
)
data class GeoDTO(
    val lat: Double?,
    val lng: Double?,
)
fun PropertyEntity.toPropertyDTO() = PropertyDTO(
    propertyId = this.propertyTypeId,
    title = this.title,
    description = this.description,
    price = this.price,
    surface = this.surface,
    rooms = this.rooms,
    bedrooms = this.bedrooms,
    electric = this.electric,
    water = this.water,
    kitchen = this.kitchen,
    livingRoom = this.livingRoom,
    guarantee = this.guarantee,
    bathroom = this.bathroom,
    floor = this.floor,
    sold = this.sold,
    transactionType = this.transactionType,
    isAvailable = this.isAvailable,
)

fun PropertyEntity.toAddressDTO() = AddressDTO(
    address = this.address,
    communeValue = this.communeValue,
    quartierValue = this.quartierValue,
    cityValue = this.cityValue,
    countryValue = this.countryValue,
    postalCode = this.postalCode
)

fun PropertyEntity.toGeo() = GeoDTO(this.latitude, this.longitude)